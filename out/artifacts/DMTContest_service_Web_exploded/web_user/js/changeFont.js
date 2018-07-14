//改变字体
$("#fontcontrols #small").click(function (event) {
    event.preventDefault();
    $("#news_content h1").animate({
        "font-size": "1.6em"
    });
    $("#news_content h2").animate({
        "font-size": "1.4em"
    });
    $("#news_content p").animate({
        "font-size": "1.2em",
        "line-height": "1.6em"
    });
});

$("#fontcontrols #medium").click(function (event) {
    event.preventDefault();
    $("#news_content h1").animate({
        "font-size": "1.8em"
    });
    $("#news_content h2").animate({
        "font-size": "1.6em"
    });
    $("#news_content p").animate({
        "font-size": "1.4em",
        "line-height": "2em"
    });

});

$("#fontcontrols #large").click(function (event) {
    event.preventDefault();
    $("#news_content h1").animate({
        "font-size": "2em"
    });
    $("#news_content h2").animate({
        "font-size": "1.8em"
    });
    $("#news_content p").animate({
        "font-size": "1.6em",
        "line-height": "2em"
    });

});

$("#fontcontrols a").click(function () {
    $("a").removeClass("selected");
    $(this).addClass("selected");

});