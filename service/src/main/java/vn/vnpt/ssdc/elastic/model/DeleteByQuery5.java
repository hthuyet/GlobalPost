package vn.vnpt.ssdc.elastic.model;

import io.searchbox.action.AbstractMultiTypeActionBuilder;
import io.searchbox.action.GenericResultAbstractAction;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DeleteByQuery5 extends GenericResultAbstractAction {

    protected DeleteByQuery5(DeleteByQuery5.Builder builder) {
        super(builder);
        this.payload = builder.query;
        this.setURI(this.buildURI());
    }

    protected String buildURI() {
        return super.buildURI() + "/_delete_by_query";
    }

    public String getPathToResult() {
        return "ok";
    }

    public String getRestMethodName() {
        return "POST";
    }

    public int hashCode() {
        return (new HashCodeBuilder()).appendSuper(super.hashCode()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            return obj.getClass() != this.getClass() ? false : (new EqualsBuilder()).appendSuper(super.equals(obj)).isEquals();
        }
    }

    public static class Builder extends AbstractMultiTypeActionBuilder<DeleteByQuery5, DeleteByQuery5.Builder> {
        private String query;

        public Builder(String query) {
            this.query = query;
        }

        public DeleteByQuery5 build() {
            return new DeleteByQuery5(this);
        }
    }
}
