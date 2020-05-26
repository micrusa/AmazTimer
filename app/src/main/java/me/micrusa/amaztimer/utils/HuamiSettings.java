package me.micrusa.amaztimer.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class HuamiSettings {
    private static final String TAG = "UserSettings-Watch";

    public static String get(ContentResolver paramContentResolver, String paramString) {
        Cursor cursor3 = null;
        ContentResolver contentResolver = null;
        Cursor cursor2 = null;
        Cursor cursor1 = null;
        try {
            String str1 = null;
            Cursor cursor = paramContentResolver.query(SettingsEntry.CONTENT_URI, SettingsEntry.COLUMNS_VALUE, "key=?", new String[] { paramString }, null);
            paramContentResolver = contentResolver;
            if (cursor != null) {
                paramContentResolver = contentResolver;
                cursor1 = cursor;
                cursor2 = cursor;
                if (cursor.moveToFirst()) {
                    cursor1 = cursor;
                    cursor2 = cursor;
                    str1 = cursor.getString(0);
                }
            }
            String str2 = str1;
            if (cursor != null) {
                cursor.close();
                str2 = str1;
            }
            return str2;
        } catch (Exception exception) {
            cursor2 = cursor1;
            exception.printStackTrace();
            cursor2 = cursor3;
            if (cursor1 != null) {
                cursor1.close();
                cursor2 = cursor3;
            }
            return (String) cursor2;
        } finally {
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public static boolean put(ContentResolver paramContentResolver, String paramString1, String paramString2) {
        return put(paramContentResolver, paramString1, paramString2, null);
    }

    public static boolean put(ContentResolver paramContentResolver, String paramString1, String paramString2, ContentObserver paramContentObserver) {
        // Byte code:
        //   0: iconst_0
        //   1: istore #4
        //   3: new android/content/ContentValues
        //   6: dup
        //   7: invokespecial <init> : ()V
        //   10: astore #9
        //   12: aload #9
        //   14: ldc 'key'
        //   16: aload_1
        //   17: invokevirtual put : (Ljava/lang/String;Ljava/lang/String;)V
        //   20: aload #9
        //   22: ldc 'value'
        //   24: aload_2
        //   25: invokevirtual put : (Ljava/lang/String;Ljava/lang/String;)V
        //   28: aload #9
        //   30: ldc 'cloud_sync_state'
        //   32: iconst_0
        //   33: invokestatic valueOf : (I)Ljava/lang/Integer;
        //   36: invokevirtual put : (Ljava/lang/String;Ljava/lang/Integer;)V
        //   39: aconst_null
        //   40: astore #7
        //   42: aconst_null
        //   43: astore_2
        //   44: iload #4
        //   46: istore #5
        //   48: aload_0
        //   49: getstatic com/huami/watch/companion/settings/WatchSettings$SettingsEntry.CONTENT_URI : Landroid/net/Uri;
        //   52: getstatic com/huami/watch/companion/settings/WatchSettings$SettingsEntry.COLUMNS_EMPTY : [Ljava/lang/String;
        //   55: ldc 'key=?'
        //   57: iconst_1
        //   58: anewarray java/lang/String
        //   61: dup
        //   62: iconst_0
        //   63: aload_1
        //   64: aastore
        //   65: aconst_null
        //   66: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   69: astore #8
        //   71: aload #8
        //   73: ifnull -> 265
        //   76: aload #8
        //   78: astore_2
        //   79: iload #4
        //   81: istore #5
        //   83: aload #8
        //   85: astore #7
        //   87: aload #8
        //   89: invokeinterface moveToFirst : ()Z
        //   94: ifeq -> 265
        //   97: aload #8
        //   99: astore_2
        //   100: iload #4
        //   102: istore #5
        //   104: aload #8
        //   106: astore #7
        //   108: ldc 'UserSettings-Watch'
        //   110: iconst_3
        //   111: invokestatic isLoggable : (Ljava/lang/String;I)Z
        //   114: ifeq -> 157
        //   117: aload #8
        //   119: astore_2
        //   120: iload #4
        //   122: istore #5
        //   124: aload #8
        //   126: astore #7
        //   128: ldc 'UserSettings-Watch'
        //   130: new java/lang/StringBuilder
        //   133: dup
        //   134: invokespecial <init> : ()V
        //   137: ldc 'Update Values : '
        //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   142: aload #9
        //   144: invokevirtual toString : ()Ljava/lang/String;
        //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   150: invokevirtual toString : ()Ljava/lang/String;
        //   153: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
        //   156: pop
        //   157: aload #8
        //   159: astore_2
        //   160: iload #4
        //   162: istore #5
        //   164: aload #8
        //   166: astore #7
        //   168: aload_0
        //   169: getstatic com/huami/watch/companion/settings/WatchSettings$SettingsEntry.CONTENT_URI : Landroid/net/Uri;
        //   172: aload #9
        //   174: ldc 'key=?'
        //   176: iconst_1
        //   177: anewarray java/lang/String
        //   180: dup
        //   181: iconst_0
        //   182: aload_1
        //   183: aastore
        //   184: invokevirtual update : (Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
        //   187: ifle -> 259
        //   190: iconst_1
        //   191: istore #4
        //   193: iload #4
        //   195: ifeq -> 236
        //   198: aload #8
        //   200: astore_2
        //   201: iload #4
        //   203: istore #5
        //   205: aload #8
        //   207: astore #7
        //   209: ldc 'UserSettings-Watch'
        //   211: ldc 'Put Success, NotifyChange now!!'
        //   213: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
        //   216: pop
        //   217: aload #8
        //   219: astore_2
        //   220: iload #4
        //   222: istore #5
        //   224: aload #8
        //   226: astore #7
        //   228: aload_0
        //   229: getstatic com/huami/watch/companion/settings/WatchSettings$SettingsEntry.CONTENT_URI : Landroid/net/Uri;
        //   232: aload_3
        //   233: invokevirtual notifyChange : (Landroid/net/Uri;Landroid/database/ContentObserver;)V
        //   236: iload #4
        //   238: istore #6
        //   240: aload #8
        //   242: ifnull -> 256
        //   245: aload #8
        //   247: invokeinterface close : ()V
        //   252: iload #4
        //   254: istore #6
        //   256: iload #6
        //   258: ireturn
        //   259: iconst_0
        //   260: istore #4
        //   262: goto -> 193
        //   265: aload #8
        //   267: astore_2
        //   268: iload #4
        //   270: istore #5
        //   272: aload #8
        //   274: astore #7
        //   276: ldc 'UserSettings-Watch'
        //   278: iconst_3
        //   279: invokestatic isLoggable : (Ljava/lang/String;I)Z
        //   282: ifeq -> 325
        //   285: aload #8
        //   287: astore_2
        //   288: iload #4
        //   290: istore #5
        //   292: aload #8
        //   294: astore #7
        //   296: ldc 'UserSettings-Watch'
        //   298: new java/lang/StringBuilder
        //   301: dup
        //   302: invokespecial <init> : ()V
        //   305: ldc 'Insert Values : '
        //   307: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   310: aload #9
        //   312: invokevirtual toString : ()Ljava/lang/String;
        //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   318: invokevirtual toString : ()Ljava/lang/String;
        //   321: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
        //   324: pop
        //   325: aload #8
        //   327: astore_2
        //   328: iload #4
        //   330: istore #5
        //   332: aload #8
        //   334: astore #7
        //   336: aload_0
        //   337: getstatic com/huami/watch/companion/settings/WatchSettings$SettingsEntry.CONTENT_URI : Landroid/net/Uri;
        //   340: aload #9
        //   342: invokevirtual insert : (Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
        //   345: astore_1
        //   346: aload_1
        //   347: ifnull -> 356
        //   350: iconst_1
        //   351: istore #4
        //   353: goto -> 193
        //   356: iconst_0
        //   357: istore #4
        //   359: goto -> 353
        //   362: astore_0
        //   363: aload_2
        //   364: astore #7
        //   366: aload_0
        //   367: invokevirtual printStackTrace : ()V
        //   370: iload #5
        //   372: istore #6
        //   374: aload_2
        //   375: ifnull -> 256
        //   378: aload_2
        //   379: invokeinterface close : ()V
        //   384: iload #5
        //   386: ireturn
        //   387: astore_0
        //   388: aload #7
        //   390: ifnull -> 400
        //   393: aload #7
        //   395: invokeinterface close : ()V
        //   400: aload_0
        //   401: athrow
        // Exception table:
        //   from	to	target	type
        //   48	71	362	java/lang/Exception
        //   48	71	387	finally
        //   87	97	362	java/lang/Exception
        //   87	97	387	finally
        //   108	117	362	java/lang/Exception
        //   108	117	387	finally
        //   128	157	362	java/lang/Exception
        //   128	157	387	finally
        //   168	190	362	java/lang/Exception
        //   168	190	387	finally
        //   209	217	362	java/lang/Exception
        //   209	217	387	finally
        //   228	236	362	java/lang/Exception
        //   228	236	387	finally
        //   276	285	362	java/lang/Exception
        //   276	285	387	finally
        //   296	325	362	java/lang/Exception
        //   296	325	387	finally
        //   336	346	362	java/lang/Exception
        //   336	346	387	finally
        //   366	370	387	finally
    }

    public static class SettingsEntry {
        public static final String[] COLUMNS_ALL;

        public static final String[] COLUMNS_EMPTY = new String[0];

        public static final String[] COLUMNS_KEY_VALUE;

        public static final String[] COLUMNS_VALUE = new String[] { "value" };

        public static final String COLUMN_CLOUD_SYNC_STATE = "cloud_sync_state";

        public static final String COLUMN_KEY = "key";

        public static final String COLUMN_VALUE = "value";

        public static final String COLUMN_WATCH_SYNC_STATE = "watch_sync_state";

        public static final Uri CONTENT_URI = Uri.parse("content://com.huami.watch.companion.settings");

        public static final int SYNC_STATE_NEED_SYNC = 0;

        public static final int SYNC_STATE_SYNCED = 1;

        static {
            COLUMNS_KEY_VALUE = new String[] { "key", "value" };
            COLUMNS_ALL = new String[] { "key", "value", "cloud_sync_state", "watch_sync_state" };
        }
    }
}

}
