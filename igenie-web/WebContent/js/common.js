function getCurrentTime() {
	var currentTime = new Date();
	var month = parseInt(currentTime.getMonth() + 1);
	month = month <= 9 ? "0" + month : month;
	var day = currentTime.getDate();
	day = day <= 9 ? "0" + day : day;
	var year = currentTime.getFullYear();
	var hour = currentTime.getHours();
	hour = hour <= 9 ? "0" + hour : hour;
	var minutes = currentTime.getMinutes();
	minutes = minutes <= 9 ? "0" + minutes : minutes;
	var seconds = currentTime.getSeconds();
	seconds = seconds <= 9 ? "0" + seconds : seconds;
	return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
}

function getDateTimeString(time) {
	var currentTime = new Date();
	currentTime.setTime(time);
	var month = parseInt(currentTime.getMonth() + 1);
	month = month <= 9 ? "0" + month : month;
	var day = currentTime.getDate();
	day = day <= 9 ? "0" + day : day;
	var year = currentTime.getFullYear();
	var hour = currentTime.getHours();
	hour = hour <= 9 ? "0" + hour : hour;
	var minutes = currentTime.getMinutes();
	minutes = minutes <= 9 ? "0" + minutes : minutes;
	var seconds = currentTime.getSeconds();
	seconds = seconds <= 9 ? "0" + seconds : seconds;
	return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
}

function getDateTimeMinString(time) {
	var currentTime = new Date();
	currentTime.setTime(time);
	var month = parseInt(currentTime.getMonth() + 1);
	month = month <= 9 ? "0" + month : month;
	var day = currentTime.getDate();
	day = day <= 9 ? "0" + day : day;
	var year = currentTime.getFullYear();
	var hour = currentTime.getHours();
	hour = hour <= 9 ? "0" + hour : hour;
	var minutes = currentTime.getMinutes();
	minutes = minutes <= 9 ? "0" + minutes : minutes;
	var seconds = currentTime.getSeconds();
	seconds = seconds <= 9 ? "0" + seconds : seconds;
	return year + "-" + month + "-" + day + " " + hour + ":" + minutes;
}

function getYesterday() {
	var currentTime = new Date();
	var time = currentTime.getTime();
	time = time - 24 * 60 * 60;
	currentTime.setTime(time);
	return currentTime;
}
