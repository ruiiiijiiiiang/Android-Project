package edu.vanderbilt.cs282.ruijiang.assignment6;

interface IDownloadBoundServiceAsyncCallback {
	oneway void onDownloadFinished(String uri);
}