var nowread_newsId = 1;
var next_newsId = 2;
var next_newsTitle = "";
var read_waitnew = false;

var url_RecommendNewsList = "http://localhost:8080/RecommendNewsList?userId="+userId;

var url_defalut_img = "images/news_banner.png";
var url_News = "http://localhost:8080/News?newsId=";
var url_NewsTagList = "http://localhost:8080/NewsTagList";
function refreshNextNews(nowId){
    var find_next_flag = false;
    if(read_waitnew == false){
        for(var i in newslist.todo){
            if(find_next_flag == true){
                next_newsId = newslist.todo[i].newsId;
                next_newsTitle = newslist.todo[i].newsTitle;
                find_next_flag = false;
            }
            if(newslist.todo[i].newsId==nowId){
                find_next_flag = true;
            }
        }
    }else{
        for(var i in waitlist.todo){
            if(find_next_flag == true){
                next_newsId = waitlist.todo[i].newsId;
                next_newsTitle = waitlist.todo[i].newsTitle;
                find_next_flag = false;
            }
            if(waitlist.todo[i].newsId==nowId){
                find_next_flag = true;
            }
        }
    }

    if(find_next_flag == true){
        next_newsId = null;
        next_newsTitle = "没有了";
    }
}

function refreshNews(nowId){
    var xhr = new XMLHttpRequest();
    var url = url_News+nowId;
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
            var str = "";
            str = xhr.responseText;

            refreshNextNews(nowId);
            
            $("#add_waitnews_span").show();

            newsContent = JSON.parse(str);
            nowread_newsId = nowId;
            newslist.nowread_newsTitle = newsContent['newsTitle'];
            waitlist.nowread_newsTitle = newsContent['newsTitle']; 
            $("#news_title").html(newsContent['newsTitle']);
            $("#news_content").html(newsContent['newsContent']);
            $("#next_news_title").html(next_newsTitle);
            //图片数小于2才改
            //console.log($("#news_content").find("img") );
            if($("#news_content").find("img").length >= 2){
                var news_imgs = $("#news_content").find("img");
                var news_first_img = news_imgs[0]["src"];
                if (news_first_img != null) {
                    $('#news_headimg').css('background-image', "url('" + news_first_img + "')");
                }
            }else{
                $('#news_headimg').css('background-image',"url('" + url_defalut_img + "')" );
            }

            $.ajax({
				url: url_NewsTagList,
				type: "GET",
				data: "newsId=" + nowread_newsId,
				
				success: function (jsonstr) {
                    var newstags = JSON.parse(jsonstr);
                    $(".newstag").remove();
                    for(i=0;i<newstags.length;i++){
                        $("#news_tags").after("<span class='newstag'>"+newstags[i]["tagContent"]+"</span>");
                    }
				},
				error: function (xhr, str) {
					console.log(xhr);
					console.log(str);
				}
			});
            
        }
    };
    xhr.send();

}

var newslist = new Vue({
    el: '#newslist',
    data: {
        sortByStatus: false,
        todo: [],
        nowread_newsTitle: ""
    },
    methods: {
        markAsDoneOrUndone: function(item) {
            item.done = !item.done;
        },
        changeReadNow: function(item) {
            read_waitnew = false;
            refreshNews(item.newsId);
        },
        movedonetoogle: function(active) {
            this.sortByStatus = active;
        },
        refreshList: function() {
            var xhr = new XMLHttpRequest();
            var url = url_RecommendNewsList;
            console.log(url);
            xhr.open("GET", url, true);
            var this_list = this;
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                    
                    var str = "";
                    str = xhr.responseText;
                    var newslistdata = JSON.parse(str);
                    newslistdata.forEach(function (element, index, array) {
                        // element: 指向当前元素的值
                        // index: 指向当前索引
                        // array: 指向Array对象本身
                        //console.log(element + ', index = ' + index);
                        element.done = false;
                    });
                    this_list.todo = newslistdata;
                }
            };
            xhr.send();
        }
    },
    computed: {
        todoByStatus: function() {

            if (!this.sortByStatus) {
                return this.todo;
            }

            var sortedArray = []
            var doneArray = this.todo.filter(function(item) { return item.done; });
            var notDoneArray = this.todo.filter(function(item) { return !item.done; });

            sortedArray = [...notDoneArray, ...doneArray]; //将一个数组转为用逗号分隔的参数序列,取代concat()
            return sortedArray;
        }
    }
});

newslist.refreshList();
