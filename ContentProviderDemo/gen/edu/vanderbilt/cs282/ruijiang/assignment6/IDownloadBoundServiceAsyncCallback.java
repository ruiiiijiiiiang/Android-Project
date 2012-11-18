/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/rui/workspace/ContentProviderDemo/src/edu/vanderbilt/cs282/ruijiang/assignment6/IDownloadBoundServiceAsyncCallback.aidl
 */
package edu.vanderbilt.cs282.ruijiang.assignment6;
public interface IDownloadBoundServiceAsyncCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback
{
private static final java.lang.String DESCRIPTOR = "edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback interface,
 * generating a proxy if needed.
 */
public static edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback))) {
return ((edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback)iin);
}
return new edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback.Stub.Proxy(obj);
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
case TRANSACTION_onDownloadFinished:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onDownloadFinished(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements edu.vanderbilt.cs282.ruijiang.assignment6.IDownloadBoundServiceAsyncCallback
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
public void onDownloadFinished(java.lang.String uri) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(uri);
mRemote.transact(Stub.TRANSACTION_onDownloadFinished, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onDownloadFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onDownloadFinished(java.lang.String uri) throws android.os.RemoteException;
}
