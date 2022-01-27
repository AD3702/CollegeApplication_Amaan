package com.example.collegeproject.Mail;


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailContactUs extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;
    private MKLoader mkLoader;
    EditText editText;

    //Progressdialog to show while sending email

    //Class Constructor
    public SendMailContactUs(Context context, String email, String subject, String message, MKLoader mkLoader, EditText editText) {
        //Initializing variables
        this.context = context;
        this.editText = editText;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.mkLoader = mkLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        mkLoader.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        mkLoader.setVisibility(View.INVISIBLE);
        //Showing a success message
        Toast.makeText(context, "Email Sent", Toast.LENGTH_LONG).show();
        AwesomeSuccessDialog awesomeSuccessDialog = new AwesomeSuccessDialog(context);
        awesomeSuccessDialog.setTitle("Message Sent")
                .setMessage("Your message has reached us we will contact you in 2 hours")
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(false)
                .setPositiveButtonText("OK")
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        editText.setText("");
                    }
                }).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
