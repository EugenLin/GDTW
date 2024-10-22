$(document).ready(function() {
    $("#long_url").on('input', function() {
        if ($(this).val().length >= 200) {
            alert("您已輸入長達200字元的網址, 請確保您的原始網址總長度未超過200字元, 否則本服務生成的短網址將指向被截斷至前200字元的不完整原始網址, 可能導致無法正常使用轉址功能! You have entered a URL that is 200 characters long. Please ensure that your original URL does not exceed 200 characters in total. Otherwise, the short URL generated by this service will point to an incomplete original URL truncated to the first 200 characters, which may result in the redirection feature not working correctly!");
        }
    });

    $("#generate").click(function() {
        var longUrl = $("#long_url").val();
        if (longUrl) {
            $.ajax({
                url: '/su_api/create_new_short_url',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ originalUrl: longUrl }),
                success: function(response) {
                    console.log("Response received: ", response); 
                    $("#shorten_url").text(response).css("background-color", "yellow");
                    $("#shorten_url").off('click').on('click', copyToClipboard);

                    
                    $("#qrcode").empty(); 
                    var qrcode = new QRCode(document.getElementById("qrcode"), {
                        text: response, 
                        width: 120,
                        height: 120
                    });

                    $("#qrcode").css("display", "block");
                },
                error: function(xhr, textStatus, errorThrown) {
                    var errorMessage = "Error: " + xhr.responseText;
                    $("#shorten_url").text(errorMessage).css("background-color", "pink");
                }
            });
        } else {
            alert("請輸入一個網址 / Please enter a valid url.");
        }
    });
});


function copyToClipboard() {
    const copyText = document.getElementById("shorten_url").textContent;
    navigator.clipboard.writeText(copyText)
        .then(() => {
            showAlert("短網址已複製到剪貼簿 / The short URL is copied!", "green");
        })
        .catch(err => {
            console.error('Failed to copy text: ', err);
            showAlert("無法複製短網址 / Unable to copy the short URL.", "red");
        });
}


function showAlert(message) {
    const alertDiv = document.createElement("div");
    alertDiv.textContent = message;
    alertDiv.style.position = "fixed"; 
    alertDiv.style.top = "50%";
    alertDiv.style.left = "50%";
    alertDiv.style.transform = "translate(-50%, -50%)";
    alertDiv.style.backgroundColor = "#C10066"; 
    alertDiv.style.color = "#FFFFFF"; 
    alertDiv.style.padding = "15px";
    alertDiv.style.borderColor = "#FFB7DD";
    alertDiv.style.borderWidth ="3px"
    alertDiv.style.borderRadius = "5px";
    alertDiv.style.zIndex = "9999"; 
    document.body.appendChild(alertDiv);
    setTimeout(function() {
        document.body.removeChild(alertDiv);
    }, 1000);
}

