$(document).ready(function() {    
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');    
    
    if (code) {        
        const shortUrl = `${window.location.host}/${code}`;
        $('.shorten-url').text(shortUrl);        
        
        $.ajax({
            url: '/su_api/get_original_url',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ code: code }),
            success: function(response) {             
                console.log("Response received: ", response);
                
                if (response.errorMessage) {                    
                    $('.original-url').text(response.errorMessage).css("color", "red");
                } else {                    
                    const originalUrl = response.originalUrl;
                    const originalUrlSafe = response.originalUrlSafe;
                    console.log(originalUrlSafe);
                    
                    $('.original-url').text(originalUrl);                  
                                     
                    if (originalUrlSafe === 0) {
                        $("#not-checked").css("display", "block");
                    } else if (originalUrlSafe === 1) {
                        $("#safe").css("display", "block");
                    } else if (originalUrlSafe === 2) {
                        $("#not-safe").css("display", "block");
                    }

                    $('.button.green').on('click', function() {
                        window.location.href = originalUrl;
                    });
                }
            },
            error: function(xhr) {
                let errorMessage;
                if (xhr.status === 404 || xhr.status === 410) {                    
                    errorMessage = xhr.responseText;
                } else {
                    errorMessage = "內部伺服器錯誤! Internal Server Error!";
                }
                console.error('Error fetching original URL:', xhr);
                $('.original-url').text(errorMessage).css("color", "red");
            }
        });
    } else {
        $('.shorten-url').text('短網址無效 / Invalid short URL.');
        $('.original-url').text('請使用有效的短網址 / Please use a valid short URL.');
    }    
});
