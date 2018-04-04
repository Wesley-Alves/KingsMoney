$(document).ready(function() {
    if ($("#slider ul li").length) {
		var length = $("#slider ul li").length;
		var width = $("#slider ul li").width();
		var dataVal = 1;
		$("#slider ul li").each(function(){
			$(this).attr("data-img", dataVal);
			dataVal++;
		});
		
		$("#slider ul").width(width * (length + 2));
		$("#slider ul li:first-child").clone().appendTo("#slider ul");
		$("#slider ul li:nth-child(" + length + ")").clone().prependTo("#slider ul");
		$("#slider ul").css("margin-left", - width);
        
		var imgPos = parseInt($("#slider ul li:nth-child(2)").attr("data-img"));
		setInterval(function() {
            $("#slider ul").animate({"margin-left": "-=" + width}, 500, function() {
				if (imgPos == length) {
					imgPos = 1;
					$("#slider ul").css("margin-left", -width);
				} else {
					imgPos++;
				}
			});
        }, 3000);
	}
});