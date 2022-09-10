var main = {
    init : function () {
        var _this = this;
        $('#btn-nickname').on('click', function () {
            _this.save();
        });
    },
    save : function () {
        var data = {
            nickname: $('#nickname').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/nickname',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(response) {
            console.log(response);
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();