package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.databaseadapter.DataBaseAdapter;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.receiver.AlarmReceiver;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.client.goapp.service.MeetingsService;
import edu.kit.pse.goapp.client.goapp.R;

public class MeetingListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    private static final String TAG = "MeetingList";
    private ServiceResultReceiver activityServiceResultReceiver;

    private ObjectConverter<List<Meeting>> meetingListConverter;
    private ObjectConverter<Participant> participantConverter;

    private ImageButton menu_button;
    private TextView textViewListIsEmpty;
    private Context context = this;
    private ProgressDialog mProgressDialog;
    private ListView list;
    private User myUser;
    private List<Meeting> meetings = new ArrayList<>();
    private MeetingListAdapter adapter = null;
    private AlarmManager alarmManager;

    private DataBaseAdapter dbAdapter;
    /**
     * wird beim aufruf erstellt
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        textViewListIsEmpty = (TextView) findViewById(R.id.text_List_is_empty);
        menu_button = (ImageButton) findViewById(R.id.menu_termine);
        menu_button.setOnClickListener(this);
        meetingListConverter = new ObjectConverter<>();
        participantConverter = new ObjectConverter<>();

        list = (ListView) findViewById(R.id.meeting_ListView);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            // Todo close App :D
            Log.e(TAG, "No User Information");
        }
        myUser = new User(userId, userName);

        Intent i = new Intent(this, MeetingsService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        startService(i);

        dbAdapter = new DataBaseAdapter(context);
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }


    /**
     * start Service method
     *
     * @param activity
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MeetingListActivity.class);
        activity.startActivity(intent);
    }

    /**
     * show pop up
     *
     * @param v view
     */
    private void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(MeetingListActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_list, popup.getMenu());
        popup.show();
    }

    /**
     * onClick handler
     *
     * @param v view that was clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine) {
            showPopUp(v);
        }
    }

    /**
     * First Button click handler
     *
     * @param view
     */
    public void meetingList_FirstButtonOnClickHandler(View view) {

        RelativeLayout meetingRow = (RelativeLayout) view.getParent();

        // should be in Try catch ? Nope because of the Unique Key R.id.TAG_MEETING
        Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);
        ImageButton firstButton;
        firstButton = (ImageButton) meetingRow.getChildAt(2);
        int imageDirection = (int) firstButton.getTag(R.id.TAG_IMAGE_DIRECTION);

        ImageButton secondButton;

        if (imageDirection == R.drawable.checked) {
            showProgressDialog();

            //create a MeetingService, that send a meeting conformation change (Accepted)
            List<Participant> participants = meeting.getParticipants();
            Participant meAsParticipant = null;

            // Find me as Participant
            for (Participant p : participants) {

                if (p.getUser().getId() == myUser.getId()) {
                    meAsParticipant = p;
                }
            }

            meAsParticipant.setConfirmation(MeetingConfirmation.CONFIRMED);

            String jparticipant = participantConverter.serialize(meAsParticipant, Participant.class);

            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
            activityServiceResultReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
            i.putExtra(CommunicationKeys.PARTICIPANT, jparticipant);
            startService(i);


            // change buttons to a accepted Meeting.
            firstButton.setImageResource(R.drawable.somemap);
            firstButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.somemap);

            secondButton = (ImageButton) meetingRow.getChildAt(3);
            secondButton.setImageResource(R.drawable.participant);
            secondButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.participant);

            // if request goes wrong, the meeting List will actualize => DB will update
            dbAdapter.insertMeeting(meeting);

        } else {
            if (imageDirection == R.drawable.somemap) {

                // start MapActivity from Meeting with startActivity with meeting ID and LatLng place

                MapActivity.start(this,meeting.getMeetingId(), meeting.getPlace().getX(), meeting.getPlace().getY());

            } else {
                // should not be Called
                Toast.makeText(getApplicationContext(), "Error: Something unexpected happened", Toast.LENGTH_SHORT).show();
                MeetingListActivity.start(this);
            }
        }
    }


    /**
     * Secound button click handler
     *
     * @param view the view that was clicked
     */
    public void meetingList_SecondButtonClickHandler(View view) {

        RelativeLayout meetingRow = (RelativeLayout) view.getParent();
        // should be in Try catch ? Nope because of the Unique Key R.id.TAG_MEETING
        final Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        ImageButton secondButton = (ImageButton) meetingRow.getChildAt(3);
        int imageDirection = (int) secondButton.getTag(R.id.TAG_IMAGE_DIRECTION);

        if (imageDirection == R.drawable.cancel) {

            android.support.v7.app.AlertDialog alertDialog = buildCancelAlertDialog(meeting);
            alertDialog.show();

        } else {
            if (imageDirection == R.drawable.participant) {

                // Start MeetingParticipant with a extra (Meeting)
                MeetingParticipantActivity.start(this, meeting);
            } else {
                // should not be Called
                Toast.makeText(getApplicationContext(), "Something unexpected happened", Toast.LENGTH_SHORT).show();
                //Todo app schließen
            }
        }
    }

    /**
     * Alert Dialog aske "if are you sure?"
     *
     * @param m Meeting
     * @return Created dialog
     */
    private android.support.v7.app.AlertDialog buildCancelAlertDialog(Meeting m) {
        final Meeting meeting = m;
        android.support.v7.app.AlertDialog.Builder cancelADB = new android.support.v7.app.AlertDialog.Builder(context);
        cancelADB
                .setTitle(meeting.getName() + "")
                .setMessage("Wirklich " + meeting.getName() + " ablehnen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rejectMeeting(meeting);
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
        return cancelADB.create();
    }

    /**
     * if a Meeting rejected
     *
     * @param meeting meeting that rejeceted
     */
    public void rejectMeeting(Meeting meeting) {
        showProgressDialog();

        List<Participant> participants = meeting.getParticipants();
        Participant meAsParticipant = null;

        // Find me as Participant
        for (Participant p : participants) {

            if (p.getUser().getId() == myUser.getId()) {
                meAsParticipant = p;
            }
        }

        meAsParticipant.setConfirmation(MeetingConfirmation.REJECTED);

        String jparticipant = participantConverter.serialize(meAsParticipant, Participant.class);

        //create a MeetingService, that send a meeting conformation change (Cancel)
        Intent i = new Intent(this, MeetingParticipantManagementService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        i.putExtra(CommunicationKeys.PARTICIPANT, jparticipant);
        startService(i);

        meetings.remove(meeting);
    }


    /**
     * show Meeting info
     *
     * @param v
     */
    public void showMeetingInfo(View v) {
        RelativeLayout meetingRow = (RelativeLayout) v.getParent();
        Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        // get information_apoitment.xml as Java view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View infoApoitment = inflater.inflate(R.layout.information_apoitment, null, false);

        // set the Meeting information in information_apoitment xml
        TextView time = (TextView) infoApoitment.findViewById(R.id.meeting_info_time);
        TextView name = (TextView) infoApoitment.findViewById(R.id.meeting_info_name);
        // TextView place = (TextView) infoApoitment.findViewById(R.id.meeting_info_adress);
        TextView creator = (TextView) infoApoitment.findViewById(R.id.info_Meeting_creator);

        // Convert TimeStampt with a Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(meeting.getTimestamp());

        time.setText("Am " + calendar.get(Calendar.DAY_OF_MONTH)+1 + "." + calendar.get(Calendar.MONTH) + "."
                + calendar.get(Calendar.YEAR) + " um " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        name.setText("Termin Name: " + meeting.getName());
        //Todo  GPS getPlace as String!
        creator.setText("Ersteller: " + meeting.getCreator().getUser().getName());

       /*   (Priority B)
        TextView memo = (TextView) infoApoitment.findViewById(R.id.info_MeetingMemo);
        memo.setText(meeting.getMemo);
        */

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingListActivity.this);
        alertDialogBuilder.setView(infoApoitment);

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog and show it
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_meeting_meetingList:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.groups_meetingList:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_MeetingList:
                SettingsActivity.start(this);
                return true;
            case R.id.about_MeetingList:
                AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultData.getString(CommunicationKeys.SERVICE)) {
            case CommunicationKeys.FROM_MEETINGS_SERVICE:
                switch (resultCode) {
                    case 200:
                        meetingsResultReceiverHandler(resultData);
                        break;
                    case 400:
                        Toast.makeText(this, "Error 400: bad request", Toast.LENGTH_LONG).show();
                        break;
                    case 403:
                        Toast.makeText(this, "Error 403: forbidden", Toast.LENGTH_LONG).show();
                        break;
                    case 404:
                        Toast.makeText(this, "Error 404: not found", Toast.LENGTH_LONG).show();

                    case 408:
                        Toast.makeText(this, "Error 408: request time out", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        // todo handle Error

                }
                hideProgressDialog();
                break;
            case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                switch (resultCode) {
                    case 200:
                        managementResultReceiverHandler(resultData);
                        hideProgressDialog();
                        break;
                    case 400:
                        Toast.makeText(this, "Error 400: bad request", Toast.LENGTH_LONG).show();
                    case 403:
                        Toast.makeText(this, "Error 403: forbidden", Toast.LENGTH_LONG).show();
                    case 404:
                        Toast.makeText(this, "Error 404: not found", Toast.LENGTH_LONG).show();
                    case 408:
                        Toast.makeText(this, "Error 408: request time out", Toast.LENGTH_LONG).show();
                    case 500:
                        Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                    default:
                        // start MeetinsServer to actualize
                        startGetMeetingsService();
                        Log.d(TAG, "Wrong Status Code. Start MeetingsService");
                }
                break;
            default:
                hideProgressDialog();
                // (wrong Service) should not call this
                Toast.makeText(this, "Error 500: wrong Service", Toast.LENGTH_LONG).show();
        }
    }

    private void managementResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                // don't need it here (Get all Meeting Info from MeetingsService)
                Log.d(TAG, "Catch GET Form MeetingParticipantManagent");
                break;
            case CommunicationKeys.DELETE:
                Log.d(TAG, "Catch DELETE Form MeetingParticipantManagent");
                // Delete a Participant (Prio B)
                break;
            case CommunicationKeys.PUT:
                // Confirmation changed
                adapter.notifyDataSetChanged();
                if (adapter.isEmpty()) {
                    textViewListIsEmpty.setText("Alle bevorstehende Termine abgesagt");
                }
                Log.d(TAG, "Catch PUT Form MeetingParticipantManagent");
                Toast.makeText(this, "Zustimmung zum Termin geändert", Toast.LENGTH_LONG).show();
                break;
            case CommunicationKeys.POST:
                Log.d(TAG, "Catch POST Form MeetingParticipantManagent");
                // add a Listed Participants (Prio B)
                break;

            default:
                // should not call this
                Log.d(TAG, "Wrong Command MeetingParticipantManagent");

                // Todo ERROR wrong Command from Service

        }
    }

    private void startGetMeetingsService() {
        showProgressDialog();
        Intent i = new Intent(this, MeetingsService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        startService(i);
    }

    private void meetingsResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                meetings = new ArrayList<>();
                List<Meeting> confirmedMeetings = new ArrayList<>();
                String jsonString = resultData.getString(CommunicationKeys.MEETINGS);

                    List<Meeting> fullMeetingList = meetingListConverter.deserializeList(jsonString, Meeting.class);

                    for (Meeting m : fullMeetingList) {

                        List<Participant> participants = m.getParticipants();
                        for (Participant p : participants) {
                            if (p.getUser().getId() == myUser.getId()) {
                                if (p.getConfirmation() != MeetingConfirmation.REJECTED) {
                                    meetings.add(m);
                                    if (p.getConfirmation() == MeetingConfirmation.CONFIRMED) {
                                        confirmedMeetings.add(m);
                                    }
                                }
                            }
                        }
                    }
                    if (meetings.isEmpty()) {
                        textViewListIsEmpty.setText("Keine bevorstehende Termine");
                        deleteAllAlarmReceiver();
                    } else {

                        adapter = new MeetingListAdapter(this, meetings);
                        list.setAdapter(adapter);
                        Log.d(TAG, "Catch GET Form MeetinsService");

                        // Update Database and set AlarmReceiver new
                        // cancel all old AlarmReceiver
                        deleteAllAlarmReceiver();

                        upDateDB(confirmedMeetings);
                        // set all new AlarmReceivers();
                        setAllAlarmReceiver();
                    }
                break;
            default:
                Log.d(TAG, "Catch Wrong Command Form MeetinsService");
                Toast.makeText(this, "Error: 500. Wrong Command From MeetingService", Toast.LENGTH_SHORT).show();
        }
        hideProgressDialog();
    }

    public void upDateDB(List<Meeting> confirmedMeetings) {
        List<Meeting> dbMeetings = dbAdapter.getAllMeetings();
        for (Meeting cm : confirmedMeetings) {
            Boolean successfulInsert = dbAdapter.insertMeeting(cm);

            Iterator<Meeting> iterator = dbMeetings.iterator();

            dbMeetings = deleteMeetingFromList(cm, dbMeetings);
        }

        for (Meeting dbm : dbMeetings) {
            dbAdapter.deleteRow(dbm.getMeetingId());
        }
    }

    private List<Meeting> deleteMeetingFromList (Meeting meeting, List<Meeting> list) {
        int index = 0;
        for (Meeting m : list) {
            if (m.getMeetingId() == meeting.getMeetingId()) {
                list.remove(index);
                return list;
            }
            index++;
        }
        return list;
    }



    public void deleteAllAlarmReceiver() {
        List<Meeting> dbMeetings = dbAdapter.getAllMeetings();

        for (Meeting meeting : dbMeetings) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(CommunicationKeys.MEETING_ID, meeting.getMeetingId());
            PendingIntent alarmIntent
                    = PendingIntent.getBroadcast(context, meeting.getMeetingId(), intent,0);
            // delete the AlarmReceiver with the Meeting_ID as Alarm ID
            alarmManager.cancel(alarmIntent);
        }
    }

    public void setAllAlarmReceiver() {
        // every 10 second
        Long repeatTime = 10*1000L;

        // buffer Time 20 Second
        Long bufferTime = 20*1000L;

        List<Meeting> dbMeetings = dbAdapter.getAllMeetings();

        for (Meeting meeting : dbMeetings) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(CommunicationKeys.MEETING_ID, meeting.getMeetingId());
            PendingIntent alarmIntent
                    = PendingIntent.getBroadcast(context, meeting.getMeetingId(), intent,0);

            if (meeting.getMeetingId() + 1000 <= System.currentTimeMillis()) {

                // Allready started Meetings will start in System Time + 20 Second
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + bufferTime, repeatTime,alarmIntent);
                Log.e("AlarmManager", "Alarm: "+ meeting.getMeetingId() + " is set" );
            } else {
                // every 10 Second repeating. starting the AlarmReceiver is meetingTime+20 Second later (Buffer)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        meeting.getTimestamp() + bufferTime, repeatTime ,alarmIntent);
                Log.e("AlarmManager", "Alarm: "+ meeting.getMeetingId() + " is set" );
            }
        }
    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Lädt...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    class MeetingListAdapter extends ArrayAdapter<Meeting> {
        Context context;

        List<Meeting> meetings;

        private final int checkedImageId = R.drawable.checked;
        private final int cancelImageId = R.drawable.cancel;
        private final int mapImageId = R.drawable.somemap;
        private final int participantImageId = R.drawable.participant;


        MeetingListAdapter(Context c, List<Meeting> meetings) {
            super(c, R.layout.meeting_row, R.id.meetingName, meetings);

            this.context = c;

            this.meetings = meetings;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //make the xml layout into a java object
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View meetingRow = inflater.inflate(R.layout.meeting_row, parent, false);

            Meeting m = meetings.get(position);
            meetingRow.setTag(R.id.TAG_MEETING, m);


            //deputy the buttons and TextViews as a java objects
            ImageButton button1 = (ImageButton) meetingRow.findViewById(R.id.button1);
            ImageButton button2 = (ImageButton) meetingRow.findViewById(R.id.button2);
            TextView name = (TextView) meetingRow.findViewById(R.id.meetingName);
            TextView time = (TextView) meetingRow.findViewById(R.id.meetingTime);


            name.setText(m.getName());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMeetingInfo(v);
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    meetingList_FirstButtonOnClickHandler(v);
                }
                });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    meetingList_SecondButtonClickHandler(v);
                }
            });

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(m.getTimestamp());

            String date = calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH)+1 + "." + calendar.get(Calendar.YEAR);
            String clockTime;
            String minute = null;
            String hour = null;
            if (calendar.get(Calendar.MINUTE) < 10) {
                minute = "0" + calendar.get(Calendar.MINUTE);
            } else {
                minute = calendar.get(Calendar.MINUTE) + "";
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
                hour = "0" + calendar.get(Calendar.HOUR_OF_DAY);
            } else {
                hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
            }
            clockTime = hour + ":" + minute;

            time.setText(date + "\n" + clockTime);

            // TODO show the date time------------------------------------------------------------------------------------------------------------------


            // search for each Meeting Yourselfe as Partcipant
            //set the images from the Buttons for each Meeting Confirmation

            for (Participant participant : m.getParticipants()) {
                if (participant.getUser().getId() == myUser.getId()) {
                    // Confirmed
                    if (participant.getConfirmation() == MeetingConfirmation.CONFIRMED) {

                        button1.setImageResource(mapImageId);
                        button1.setTag(R.id.TAG_IMAGE_DIRECTION, mapImageId);

                        button2.setImageResource(participantImageId);
                        button2.setTag(R.id.TAG_IMAGE_DIRECTION, participantImageId);

                    } else {
                        // Pending
                        if (participant.getConfirmation() == MeetingConfirmation.PENDING) {
                            button1.setImageResource(checkedImageId);
                            button1.setTag(R.id.TAG_IMAGE_DIRECTION, checkedImageId);

                            button2.setImageResource(cancelImageId);
                            button2.setTag(R.id.TAG_IMAGE_DIRECTION, cancelImageId);
                        }
                    }
                }
            }

            return meetingRow;
        }
    }

}