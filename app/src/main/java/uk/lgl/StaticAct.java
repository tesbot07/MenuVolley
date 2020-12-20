package uk.lgl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.lgl.modmenu.FloatingModMenuService;


public class StaticAct {

	private static String UPDATE_URL = "https://pastebin.com/raw/hGGgvkwF";
	private static String URL_LOGIN = "https://scrzoke.000webhostapp.com/login.php";
	@SuppressLint("StaticFieldLeak")
	private static HashMap<String, Object> hashmap = new HashMap<>();
	private static Intent intent = new Intent();


	@SuppressLint("SetTextI18n")
	public static void Start(final Context context) {
		System.loadLibrary("MyLibName");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
			context.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION",
					Uri.parse("package:" + context.getPackageName())));
		}
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;

		String key = (manufacturer + " " + model + Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID) + Build.HARDWARE).replace(" ", "");
		UUID uniqueKey = UUID.nameUUIDFromBytes(key.getBytes());
		final String ggg = uniqueKey.toString().replace("-", "");

		final EditText textUid = new EditText(context);
		textUid.setGravity(Gravity.CENTER_HORIZONTAL);
		textUid.setBackgroundColor(Color.parseColor("#ffffff"));
		textUid.setTextColor(Color.rgb(0, 0, 0));
		textUid.setTextSize(14);
		textUid.setText(ggg);

		//Create LinearLayout
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(500, 150));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		linearLayout.setPadding(4, 0, 4, 0);

		//Create username textView
		final TextView textStatus = new TextView(context);
		textStatus.setGravity(Gravity.CENTER_HORIZONTAL);
		textStatus.setBackgroundColor(Color.parseColor("#ffffff"));
		textStatus.setTextColor(Color.rgb(0, 0, 255));
		textStatus.setTextSize(17);

		//Add views
		linearLayout.addView(textStatus);
		linearLayout.addView(textUid);

		//Create alertdialog
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		//Title
		TextView title = new TextView(context);
		title.setText("|_______Scrz-Mods_______|");
		title.setPadding(0, 20, 0, 20);
		title.setTextColor(Color.rgb(255, 255, 255));
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setBackgroundColor(Color.rgb(255,0,0));
		//builder.setCustomTitle(title);

		builder.setCancelable(false);
		builder.setView(linearLayout);

		final EditText textUid2 = textUid;

		if (callURL(UPDATE_URL).equals("V6")) {
			StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								String success = jsonObject.getString("success");
								JSONArray jsonArray = jsonObject.getJSONArray("login");

								if(success.equals("1")){

									for (int i = 0; i < jsonArray.length(); i++) {

										JSONObject object = jsonArray.getJSONObject(i);
										String strName = object.getString("name").trim();
										String strUid = object.getString("UID").trim();
										String strId = object.getString("id").trim();

										System.loadLibrary("MyLibName");
										Handler handler = new Handler();
										handler.postDelayed(new Runnable() {
											@Override
											public void run() {
												context.startService(new Intent(context, FloatingModMenuService.class));
											}
										}, 1);

										Toast.makeText(context, "Welcome " + strName, Toast.LENGTH_SHORT).show();
//
									}

								}else{
									AlertDialog show = builder.show();
									final AlertDialog alertDialog = show;
									textStatus.setTextColor(Color.parseColor("#ff3c00"));
									textStatus.setText("Expired!");
									//  textStatus.setText(response);
									//Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
								}

							} catch (JSONException e) {
								e.printStackTrace();
								AlertDialog show = builder.show();
								final AlertDialog alertDialog = show;
								textStatus.setTextColor(Color.parseColor("#ff3c00"));
								textStatus.setText("ID tidak terdaftar!");
								// Toast.makeText(context, "res 2 "+e.toString()  , Toast.LENGTH_SHORT).show();
							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							//Toast.makeText(context , "Error " +error.toString(), Toast.LENGTH_SHORT).show();
						}
					})
			{
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					String UID = textUid2.getText().toString().trim();
					Map<String, String> params = new HashMap<>();
					params.put("UID", UID);
					return params;
				}
			};

			RequestQueue requestQueue = Volley.newRequestQueue(context);
			requestQueue.add(stringRequest);

		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(context, 5) //
					.setTitle("Update Available")
					.setMessage("✅ Terdapat update baru")
					.setPositiveButton("⭕ DOWNLOAD", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							intent.setAction(Intent.ACTION_VIEW);
							intent.setData(Uri.parse("https://drive.google.com/folderview?id=1-6Gi4cQv-rOrEs_YsUaXHpGLwgFii6f9"));
							context.startActivity(intent);
						}
					}).setCancelable(false)
					.create();
			alertDialog.show();
		}

	}


	public static String callURL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + myURL, e);
		}
		Log.d("gettextfromurl", sb.toString());
		return sb.toString();
	}
}


