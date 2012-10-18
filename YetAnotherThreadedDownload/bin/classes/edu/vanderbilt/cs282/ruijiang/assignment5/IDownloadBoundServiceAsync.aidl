package edu.vanderbilt.cs282.ruijiang.assignment5;

import edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceAsyncCallback;

interface IDownloadBoundServiceAsync {
	oneway void setCallback(IDownloadBoundServiceAsyncCallback callback, String url);
}