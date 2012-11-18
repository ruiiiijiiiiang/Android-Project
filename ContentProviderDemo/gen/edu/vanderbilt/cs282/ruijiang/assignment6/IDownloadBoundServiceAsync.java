/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/rui/workspace/ContentProviderDemo/src/edu/vanderbilt/cs282/ruijiang/assignment6/IDownloadBoundServiceAsync.aidl
 */
package edu.vanderbilt.cs282.ruijiang.assignment6;
public interface IDownloadBoundServiceAsync extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync
{
private static final java.lang.String DESCRIPTOR = "edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync interface,
 * generating a proxy if needed.
 */
public static edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync))) {
return ((edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync)iin);
}
return new edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setCallback:
{
data.enforceInterface(DESCRIPTOR);
edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback _arg0;
_arg0 = edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg1;
_arg1 = data.readString();
this.setCallback(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsync
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void setCallback(edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback callback, java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_setCallback, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_setCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void setCallback(edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback callback, java.lang.String url) throws android.os.RemoteException;
}
