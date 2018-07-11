var imgStr = "[{\"id\":\"10153231820659452\",\"url\":\"https://graph.facebook.com/10153231820659452/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu09XxWyLUEbBw\"},{\"id\":\"10153240026569452\",\"url\":\"https://graph.facebook.com/10153240026569452/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu09XxWyLUEbBw\"},{\"id\":\"100923764451\",\"url\":\"https://graph.facebook.com/100923764451/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu09XxWyLUEbBw\"},{\"id\":\"10150296859054452\",\"url\":\"https://graph.facebook.com/10150296859054452/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu09XxWyLUEbBw\"},{\"id\":\"10150296862799452\",\"url\":\"https://graph.facebook.com/10150296862799452/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu09XxWyLUEbBw\"},{\"id\":\"10150296862804452\",\"url\":\"https://graph.facebook.com/10150296862804452/picture?access_token=EAAO4WlvqQYYBALeXGBZCOHhkDwfX8QZCnySDCaH35ZAqUEZCPzqUJs18Q9DwgVo2dZCIu3to07caCYRnWbx56hBKrUnk2opSWVrSX96qlAH6sAZCbBvNWVTLbcr39KnBPSA4MTV0FZCBuhLT7bfCqRDZC2ZBm0sb0zZACLO2ve4NqriOp0IZCZCtjQknsb31tJwZCIabGTdtCpMIu0\"}]";


/**
 * This is the getPhoto library
 */

function makeFacebookPhotoURL( id, accessToken ) {
    return 'https://graph.facebook.com/' + id + '/picture?access_token=' + accessToken;
}

function login( callback ) {
    FB.login(function(response) {
        if (response.authResponse) {
            //console.log('Welcome!  Fetching your information.... ');
            if (callback) {
                callback(response);
            }
        } else {
            console.log('User cancelled login or did not fully authorize.');
        }
    },{scope: 'user_photos'} );
}

function getAlbums( callback ) {
    FB.api(
        '/me/albums',
        {fields: 'id,cover_photo'},
        function(albumResponse) {
            //console.log( ' got albums ' );
            if (callback) {
                callback(albumResponse);
            }
        }
    );

}

function getPhotosForAlbumId( albumId, callback ) {
    FB.api(
        '/'+albumId+'/photos',
        {fields: 'id'},
        function(albumPhotosResponse) {
            //console.log( ' got photos for album ' + albumId );
            if (callback) {
                callback( albumId, albumPhotosResponse );
            }
        }
    );
}

function getLikesForPhotoId( photoId, callback ) {
    FB.api(
        '/'+albumId+'/photos/'+photoId+'/likes',
        {},
        function(photoLikesResponse) {
            if (callback) {
                callback( photoId, photoLikesResponse );
            }
        }
    );
}

function getPhotos(callback) {

    var allPhotos = [];

    var accessToken = '';

    login(function(loginResponse) {
        accessToken = loginResponse.authResponse.accessToken || '';
        getAlbums(function(albumResponse) {
            var i, album, deferreds = {}, listOfDeferreds = [];

            for (i = 0; i < albumResponse.data.length; i++) {
                album = albumResponse.data[i];
                deferreds[album.id] = $.Deferred();
                listOfDeferreds.push( deferreds[album.id] );
                getPhotosForAlbumId( album.id, function( albumId, albumPhotosResponse ) {
                    var i, facebookPhoto;
                    for (i = 0; i < albumPhotosResponse.data.length; i++) {
                        facebookPhoto = albumPhotosResponse.data[i];
                        allPhotos.push({
                            'id'	:	facebookPhoto.id,
                            'added'	:	facebookPhoto.created_time,
                            'url'	:	makeFacebookPhotoURL( facebookPhoto.id, accessToken )
                        });
                    }
                    deferreds[albumId].resolve();
                });
            }

            $.when.apply($, listOfDeferreds ).then( function() {
                if (callback) {
                    callback( allPhotos );
                }
            }, function( error ) {
                if (callback) {
                    callback( allPhotos, error );
                }
            });
        });
    });
}

/**
 * This is the bootstrap / app script
 */

    // wait for DOM and facebook auth
var docReady = $.Deferred();
var facebookReady = $.Deferred();

$(document).ready(docReady.resolve);

window.fbAsyncInit = function() {
    FB.init({
        appId      : '1841232782774816',
        // channelUrl : '//conor.lavos.local/channel.html',
        status     : true,
        cookie     : true,
        xfbml      : true
    });
    facebookReady.resolve();
};

// $.when(docReady, facebookReady).then(

// call facebook script
(function(d){
    var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "http://connect.facebook.net/en_US/all.js";
    d.getElementsByTagName('head')[0].appendChild(js);
}(document));


$(document).ready(function(){

    $( "#login" ).animate({ marginTop: '200px' , opacity: 1 }, 2500);


$("#fb-login").click(function run() {
        // if (typeof getPhotos !== 'undefined') {

        // processResponse();
        getPhotos( function( photos ) {
            console.log( photos );
            console.log(JSON.stringify(photos));
                processResponse(JSON.stringify(photos));
        });

    });
})




// smart scripts
function processResponse(data){

    $.ajax({
        url: 'http://localhost:5000/analyze',
        type: 'POST',
        contentType: 'application/json',
        data: data,
        //JSON.stringify(response)
        dataType: "text"
    });
}