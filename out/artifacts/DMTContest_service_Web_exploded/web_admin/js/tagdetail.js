var searchStr = location.search;
//由于searchStr属性值包括“?”，所以除去该字符
searchStr = searchStr.substr(1);
//将searchStr字符串分割成数组，数组中的每一个元素为一个参数和参数值
var searchs = searchStr.split("&");
//获得第一个参数和值
var address = searchs[0].split("=");
//获取到登陆用户的id
var id = address[1];
var infourl = "http://localhost:8080/AllTags?tagId=" + id;

function formatDate(unformdate) {
    var temp = new Date(unformdate);
    temp.setMonth(temp.getMonth()+1);
    return temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
}     


$(document).ready(function() {
    $.ajax({
        type: 'get',
        url: infourl,
        timeout: 10000, //超时时间，毫秒
        success: function(XHR, TS) {
            if (XHR != null || XHR != "") {
                $("#tagId").text(XHR["tagId"]);
                $("#tagContent").text(XHR["tagContent"]);
                $("#tagDate").text(formatDate(XHR["tagDate"]));
            } else {
                $("#tags-table").text("加载失败");
            }
        },
        error: function() {
            $("#tags-table").text("加载失败");
        }
    });
});

