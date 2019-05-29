// $("button#translate").click(

function awsTranslate(txt){
    $('#translate').prop('disabled', true);
    // var txt = $("textarea#input").val();
    if(txt.length < 4) return;
    console.log(txt);
//location.host +

    var lang = $("#language option:selected").val();
    lang = lang.length > 0 ? "&toLang=" + lang : "es-US";
    var url = location.protocol +"//" + location.host + "/api/translate.mp3?txt=" + encodeURI(txt) + lang;
    //
    $('audio #source').attr('src', url);
    $('audio').get(0).load();
    $('audio').get(0).play();
    setTimeout(
        $('#translate').prop('disabled', false), 10000)


}