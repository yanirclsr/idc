$("button#translate").click(function(){
    var txt = $("textarea#input").val();
    console.log(txt);
//location.host +

    $('audio #source').attr('src', location.host + "/api/translate.mp3?txt=" + encodeURI(txt));
    $('audio').get(0).load();
    $('audio').get(0).play();

})