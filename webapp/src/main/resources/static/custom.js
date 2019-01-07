function convertToDate(input){
  var tmp = input.split("/");
  if(tmp.length < 3){
    return null;
  }
  var mydate = new Date(tmp[2], tmp[1] - 1, tmp[0]);
  console.log(mydate.toDateString());
  return mydate;
}