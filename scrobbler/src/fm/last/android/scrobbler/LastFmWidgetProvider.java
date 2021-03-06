/**
 * 
 */
package fm.last.android.scrobbler;

import java.util.Formatter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author sam
 *
 */
public class LastFmWidgetProvider extends AppWidgetProvider {
    // log tag
    private static final String TAG = "LastFmWidgetProvider";
    
    static final ComponentName THIS_APPWIDGET =
        new ComponentName("fm.last.android.scrobbler",
                "fm.last.android.scrobbler.LastFmWidgetProvider");
    
    private static LastFmWidgetProvider sInstance;
    
    public static synchronized LastFmWidgetProvider getInstance() {
        if (sInstance == null) {
            sInstance = new LastFmWidgetProvider();
        }
        return sInstance;
    }
    
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
    }
    
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
    }

    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
    }

    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
    }

    public static void updateAppWidget(Context context, String title, String artist, Bitmap artwork) {
        Log.d(TAG, "updateAppWidget");
        Intent intent;
        PendingIntent pendingIntent;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);        
        // Construct the RemoteViews object.  It takes the package name (in our case, it's our
        // package, but it needs this because on the other side it's the widget host inflating
        // the layout from our package).
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.track_title, title);
        views.setTextViewText(R.id.track_artist, artist);

        //Hook up the buttons (this should really be done eslewhere but doesn't hurt here)
        intent = new Intent(ScrobblerService.LOVE);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.love, pendingIntent);

        intent = new Intent(ScrobblerService.BAN);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ban, pendingIntent);

        intent = new Intent(context, Metadata.class);
        intent.putExtra("artist", artist);
        intent.putExtra("track", title);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.menu, pendingIntent);

        if(artwork == null)
        	views.setImageViewResource(R.id.artwork, R.drawable.no_artwork);
        else
        	views.setImageViewBitmap(R.id.artwork, artwork);
        // Tell the widget manager
        appWidgetManager.updateAppWidget(THIS_APPWIDGET, views);
    }

    public static String makeTimeString(long secs) {
		return new Formatter().format("%02d:%02d", secs / 60, secs % 60)
				.toString();
	}

}
