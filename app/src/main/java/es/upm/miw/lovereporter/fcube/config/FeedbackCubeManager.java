package es.upm.miw.lovereporter.fcube.config;

import android.os.AsyncTask;
import android.util.Log;

import java.io.EOFException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import es.upm.miw.lovereporter.fcube.commands.IFeedbackCubeCommnads;

public class FeedbackCubeManager extends AsyncTask<IFeedbackCubeCommnads, Void, String> {

    private String CLASSNAME = this.getClass().getName();

    @Override
    protected String doInBackground(IFeedbackCubeCommnads... params) {
        try {
            IFeedbackCubeCommnads Ifcc = params[0];
            Log.d(CLASSNAME, "Lunch command " + Ifcc.toString());
            run(Ifcc);
            Log.d(CLASSNAME, "Command launched! " + Ifcc.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Executed " + params[0].getAction();
    }


    private void run(IFeedbackCubeCommnads cmd) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) cmd.getUrlCommand().openConnection();
            connection.setRequestMethod(cmd.getHttpMethod());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept-Encoding", "");
            if (cmd.hasParams()) {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(cmd.getParams());
                osw.flush();
                osw.close();
            }
            connection.connect();
            connection.getResponseCode();
        } catch (EOFException e) {
            Log.e(CLASSNAME, "There seems to be some bug reading stream from webservice");
            if (connection != null) {
                connection.disconnect();
            }

        } catch (Exception e) {
            if (connection != null) {
                connection.disconnect();
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("On poste execute: " + result);
    }

    @Override
    protected void onPreExecute() {
        System.out.println("On pre execute.");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        System.out.println("On progress update.");
    }
}