package edu.vanderbilt.cs282.ruijiang.assignment6;

import edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback;

interface IDownloadBoundServiceAsync {
	oneway void setCallback(IDownloadBoundServiceAsyncCallback callback, String url);
}