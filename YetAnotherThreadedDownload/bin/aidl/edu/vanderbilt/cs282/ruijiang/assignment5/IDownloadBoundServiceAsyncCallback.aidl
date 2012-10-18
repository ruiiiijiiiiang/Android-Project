package edu.vanderbilt.cs282.ruijiang.assignment5;

interface IDownloadBoundServiceAsyncCallback {
	oneway void onDownloadFinished(String file_name);
	oneway void onError(String error_message);
}