/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/rui/workspace/YetAnotherThreadedDownload/src/edu/vanderbilt/cs282/ruijiang/assignment5/IDownloadBoundServiceSync.aidl
 */
package edu.vanderbilt.cs282.ruijiang.assignment5;
public interface IDownloadBoundServiceSync extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync
{
private static final java.lang.String DESCRIPTOR = "edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync interface,
 * generating a proxy if needed.
 */
public static edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync))) {
return ((edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync)iin);
}
return new edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync.Stub.Proxy(obj);
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
case TRANSACTION_downloadFileAndReturnFileName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.downloadFileAndReturnFileName(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements edu.vanderbilt.cs282.ruijiang.assignment5.IDownloadBoundServiceSync
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
public java.lang.String downloadFileAndReturnFileName(java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_downloadFileAndReturnFileName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_downloadFileAndReturnFileName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String downloadFileAndReturnFileName(java.lang.String url) throws android.os.RemoteException;
}
