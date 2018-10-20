package vn.vnpt.ssdc.elastic.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.vnpt.ssdc.common.services.ConfigurationService;
import vn.vnpt.ssdc.elastic.model.DeleteByQuery5;
import vn.vnpt.ssdc.utils.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class ElasticService {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATETIME_ISO_FILTER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    JestClient elasticSearchClient;

    @Autowired
    public ConfigurationService configurationService;

    @Value("${elasticSearchUrl}")
    public String elasticSearchUrl;

    protected Integer getVersionElk() {
        Integer result = null;
        try {
            if(elasticSearchUrl == null || elasticSearchUrl.trim().length() <= 0){
                return null;
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(elasticSearchUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String version = root.path("version").path("number").toString().replaceAll("\"", "");
            result = Integer.valueOf(version.split("[.]")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Boolean deleteByBoolQuery(BoolQueryBuilder boolQueryBuilder, String elkIndex, String elkType) {
        Boolean result = false;
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder);

            JestResult jestResult;
            if (getVersionElk() >= 5) {
                DeleteByQuery5 deleteByQuery = new DeleteByQuery5.Builder(searchSourceBuilder.toString())
                        .addIndex(elkIndex).addType(elkType).build();
                jestResult = elasticSearchClient.execute(deleteByQuery);
            } else {
                DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(searchSourceBuilder.toString())
                        .addIndex(elkIndex).addType(elkType).build();
                jestResult = elasticSearchClient.execute(deleteByQuery);
            }
            result = jestResult.isSucceeded();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String convertTimeExpire(String timeExpire) {
        int days = 0;
        String time = "";

        String[] timeArr = timeExpire.split("\\s+");
        for(String t : timeArr) {
            if(t.endsWith("d")) days += Integer.valueOf(t.substring(0, t.length()-1));
            if(t.endsWith("w")) days += Integer.valueOf(t.substring(0, t.length()-1))*7;
            if(t.endsWith("m")) days += Integer.valueOf(t.substring(0, t.length()-1))*30;
            if(t.endsWith("y")) days += Integer.valueOf(t.substring(0, t.length()-1))*365;
        }
        if(days >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -days);
            Date date = new Date(cal.getTimeInMillis());
            time = sdf.format(date);
        }
        return time;
    }

    public BoolQueryBuilder createBoolQuery(String name, String actor, String fromDateTime, String toDateTime, String username) {

        // Create query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.queryStringQuery("\"### SESSION\"").field("message"));

        // Get time expire to set from date time
        String timeExpire = configurationService.get("timeExpire").value;
        if (!("").equals(fromDateTime) && timeExpire != null) {
            if (parseIsoDate(fromDateTime).compareTo(parseIsoDate(convertTimeExpire(timeExpire))) < 0) {
                fromDateTime = convertTimeExpire(timeExpire);
            }
        } else {
            fromDateTime = convertTimeExpire(timeExpire);
        }

        if (!("").equals(name)) {
            BoolQueryBuilder boolQueryBuilderName = new BoolQueryBuilder();
            boolQueryBuilderName.should(QueryBuilders.matchPhrasePrefixQuery("message", name));
            boolQueryBuilderName.should(QueryBuilders.matchPhrasePrefixQuery("message", "cwmp:" + name)).minimumShouldMatch("1");
            boolQueryBuilder.must(boolQueryBuilderName);
        }
        if (!("").equals(actor)) {
            for (String subActor : actor.split("-")) {
                if (!subActor.isEmpty()) {
                    boolQueryBuilder.must(QueryBuilders.queryStringQuery(String.format("\">%s<\"", subActor)).field("message"));
                }
            }
        }
        if (!("").equals(fromDateTime) && !("").equals(toDateTime))
            boolQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp")
                    .gte(parseIsoDate(fromDateTime)).lt(parseIsoDate(toDateTime)));
        if (!("").equals(fromDateTime) && ("").equals(toDateTime))
            boolQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp")
                    .gte(parseIsoDate(fromDateTime)));
        if (("").equals(fromDateTime) && !("").equals(toDateTime))
            boolQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp")
                    .lt(parseIsoDate(toDateTime)));

        return boolQueryBuilder;

    }

    private String parseIsoDate(String date){
        return StringUtils.convertDateToElk(date, DATETIME_FORMAT, DATETIME_ISO_FILTER_FORMAT);
    }
}
