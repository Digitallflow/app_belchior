package com.digitalflow.belchior.appbelchior.MediaService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.digitalflow.belchior.appbelchior.R;

public class NotificationGenerator {
    public static final String NOTIFY_PREVIOUS = "action_previous";
    public static final String NOTIFY_DELETE = "action_delete";
    public static final String NOTIFY_PAUSE = "action_pause";
    public static final String NOTIFY_PLAY = "action_play";
    public static final String NOTIFY_NEXT = "action_next";
    private static final int NOTIFICATION_ID_SIMPLE = 1;
    private static final int NOTIFICATION_ID_TWO_ICONS = 2;
    private static final int NOTIFICATION_ID_BIG_PIC = 3;
    private static final int NOTIFICATION_ID_BIG_TEXT = 4;
    private static final int NOTIFICATION_ID_CHAT = 5;
    private static final int NOTIFICATION_ID_ACTION_BUTTON = 6;
    private static final int NOTIFICATION_ID_CUSTOM_SIMPLE = 7;
    private static final int NOTIFICATION_ID_CUSTOM_BIG = 8;
    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;
    static String songName = "Now You're Gone";
    static String albumName = "Now Youre Gone - The Album";

    public static void simpleNotification(Context context,
                                          Class<?> cls,
                                          int smallIcon,
                                          boolean setAutoCancel,
                                          String contentTitle,
                                          String contentText,
                                          String ticker) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "SIMPLE");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an Intent for the Activity
        Intent notifyIntent = new Intent(context, cls);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        nc.setSmallIcon(smallIcon);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);
        nc.setTicker(ticker);
        nc.setNumber(2);

        nm.notify(NOTIFICATION_ID_SIMPLE, nc.build());
    }

    public static void openActivityNotification(Context context,
                                                Class<?> cls,
                                                int smallIcon,
                                                boolean setAutoCancel,
                                                String contentTitle,
                                                String contentText) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "OPEN_ACTIVITY");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an Intent for the Activity
        Intent notifyIntent = new Intent(context, cls);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        nc.setSmallIcon(smallIcon);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);

        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());
    }

    public static void twoIconNotification(Context context,
                                           int iconSmall,
                                           int iconBig,
                                           boolean setAutoCancel,
                                           String contentTitle,
                                           String contentText,
                                           String ticker) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "TWO_ICON");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nc.setSmallIcon(iconSmall);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconBig));
        nc.setTicker(ticker);
        nc.setNumber(3);

        nm.notify(NOTIFICATION_ID_TWO_ICONS, nc.build());
    }

    public static void bigPictureNotification(Context context,
                                              int iconSmall,
                                              int iconBig,
                                              int iconBtnAction,
                                              boolean setAutoCancel,
                                              String contentTitle,
                                              String contentBigTitle,
                                              String contentText,
                                              String titleBtnAction,
                                              PendingIntent intentActionBtn) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "BIG_PICTURE");
        NotificationCompat.BigPictureStyle bigPictureNotification = new NotificationCompat.BigPictureStyle();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nc.setSmallIcon(iconSmall);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);
        nc.addAction(iconBtnAction, titleBtnAction, intentActionBtn);

        bigPictureNotification.bigPicture(BitmapFactory.decodeResource(context.getResources(), iconBig));
        bigPictureNotification.setBigContentTitle(contentBigTitle);
        nc.setStyle(bigPictureNotification);

        nm.notify(NOTIFICATION_ID_BIG_PIC, nc.build());
    }

    public static void bigTextStyleNotification(Context context,
                                                int iconSmall,
                                                boolean setAutoCancel,
                                                String contentTitle,
                                                String contentBigTitle,
                                                String contentText,
                                                String bigText,
                                                String summaryText) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "BIG_TEXT_STYLE");
        NotificationCompat.BigTextStyle bigTextNotification = new NotificationCompat.BigTextStyle();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nc.setSmallIcon(iconSmall);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);

        bigTextNotification.setBigContentTitle(contentBigTitle);
        bigTextNotification.bigText(bigText);
        bigTextNotification.setSummaryText(summaryText);
        nc.setStyle(bigTextNotification);

        nm.notify(NOTIFICATION_ID_BIG_TEXT, nc.build());
    }

    public static void chatStyleNotification(Context context,
                                             String[] lines,
                                             int iconSmall,
                                             boolean setAutoCancel,
                                             String contentTitle,
                                             String contentBigTitle,
                                             String summaryText) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "CHAT_STYLE");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nc.setSmallIcon(iconSmall);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(lines[0]);
        nc.setNumber(7);

        for (int i = 0; i < lines.length; i++) {
            inboxStyle.addLine(lines[i]);
        }
        inboxStyle.setBigContentTitle(contentBigTitle);
        inboxStyle.setSummaryText(summaryText);
        nc.setStyle(inboxStyle);

        nm.notify(NOTIFICATION_ID_CHAT, nc.build());
    }

    private interface setPendingIntent{
        PendingIntent Intent1();
        PendingIntent Intent2();
    }

    public static void actionButtonNotification(Context context,
                                                int iconSmall,
                                                boolean setAutoCancel,
                                                String contentTitle,
                                                String contentText,
                                                String contentBigTitle,
                                                String contentBigText,
                                                int iconAction1,
                                                String actionTitle1,
                                                int iconAction2,
                                                String actionTitle2,
                                                setPendingIntent pendingIntent1,
                                                setPendingIntent pendingIntent2) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "ACTION_BUTTON");
        NotificationCompat.BigTextStyle bigTextNotification = new NotificationCompat.BigTextStyle();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nc.setSmallIcon(iconSmall);
        nc.setAutoCancel(setAutoCancel);
        nc.setContentTitle(contentTitle);
        nc.setContentText(contentText);
        nc.addAction(iconAction1, actionTitle1, pendingIntent1.Intent1());
        nc.addAction(iconAction2, actionTitle2, pendingIntent2.Intent2());

        bigTextNotification.setBigContentTitle(contentBigTitle);
        bigTextNotification.bigText(contentBigText);
        nc.setStyle(bigTextNotification);

        nm.notify(NOTIFICATION_ID_ACTION_BUTTON, nc.build());
    }

    public interface IntentNotification {
        Intent setIntent();
    }

    public static void customSimpleNotification(Context context,
                                                int iconSmall,
                                                String contentTitle,
                                                IntentNotification in) {
        RemoteViews simpleView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        // Creates an Intent for the Activity
        Intent notifyIntent = in.setIntent();
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context, "CUSTOM_SIMPLE")
                .setSmallIcon(iconSmall)
                .setContentTitle(contentTitle)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.contentView = simpleView;
        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);

        setListeners(simpleView, context);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID_CUSTOM_SIMPLE, notification);
    }

    @SuppressLint("NewApi")
    public static void customBigNotification(Context context,
                                             int iconSmall,
                                             String contentTitle,
                                             String songName,
                                             String albumName,
                                             IntentNotification in) {
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.big_notification);
        // Creates an Intent for the Activity
        Intent notifyIntent = in.setIntent();
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // Creates the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "CUSTOM_BIG")
                .setSmallIcon(iconSmall)
                .setContentTitle(contentTitle)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.bigContentView = expandedView;
        notification.bigContentView.setTextViewText(R.id.textSongName, songName);
        notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);

        setListeners(expandedView, context);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID_CUSTOM_BIG, notification);
    }

    private static void setListeners(RemoteViews view, Context context) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(context, 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);
    }
}
