cordova.define("cordova/plugins/OpenPDF",function(require, exports, module) {

	var exec = require("cordova/exec");

	var OpenPDF = function() {};

	OpenPDF.prototype.openWithUrl = function(url,successCallback, errorCallback) {

		if (errorCallback == null) { errorCallback = function() {}}

		if (typeof errorCallback != "function" || typeof successCallback != "function" || !url)  {

			errorCallback('openWithUrl参数错误！');

			return;

		}

		exec(successCallback, errorCallback, 'openPDF', 'openWithUrl', [{"url":url}]);

	};

    module.exports = new OpenPDF();

});


if(!window.plugins) window.plugins = {};

if (!window.plugins.OpenPDF) window.plugins.OpenPDF = cordova.require("cordova/plugins/OpenPDF");
