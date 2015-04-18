package com.example.ezrakirsh.school;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity  {

    public AlertDialog deleteOptions(String name, String grade, final List<String> homework, final ParseObject object) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
        myBuilder.setTitle("Student Options - " + name + " (" + grade + ")");
        LayoutInflater inflate = getLayoutInflater();

        myBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                object.deleteInBackground();
                loadStudents();
                dialog.dismiss();
            }
        });
        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();;
            }
        });
        AlertDialog thisDialog = myBuilder.create();

        return thisDialog;
    }

    public AlertDialog studentOptions(String name, String grade, final List<String> homework, final ParseObject object) {

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
        myBuilder.setTitle("Student Options - " + name + " (" + grade + ")");
        LayoutInflater inflate = getLayoutInflater();
        final View modifalert = inflate.inflate(R.layout.modifalert, null);
        myBuilder.setView(modifalert);


        myBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText homeworkTextEdit = (EditText) modifalert.findViewById(R.id.editText2);
                System.out.println(homework);
                homework.clear();
                String homeworkText = homeworkTextEdit.getText().toString();
                homework.add(homeworkText);

                object.put("Homework", homework);
                object.saveInBackground();
            }
        });


        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog myDialog = myBuilder.create();
        return myDialog;
    }


    public void loadStudents() {
        final StaggeredGridView listView = (StaggeredGridView) findViewById(R.id.grid_view);
        final ArrayList<ParseObject> list = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Students");
        q.whereExists("Name");

        final ArrayList<Student> students = new ArrayList<Student>();
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                for (int x = 0; x < parseObjects.size(); x++) {
                   List<String> currentHomework = parseObjects.get(x).getList("Homework");

                   Student tempStudent = new Student(parseObjects.get(x).getString("Name"), parseObjects.get(x).getString("Grade"), currentHomework);
                   students.add(tempStudent);
                   list.add(parseObjects.get(x));
                }


                SchoolAdapter adapter = new SchoolAdapter(students);
                listView.setAdapter(adapter);
                clickSetUp(students, list);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "A4ZZnSwGYEtmpFQAQVS7tH0DXLu9AkcWxjzclaYB", "ef8Rgn6ILqDGPRiHfq3V2OEeJpgK3y6DQUBoiUCX");
        ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.android);
        resideMenu.attachToActivity(this);
        String titles[] = { "Add", "Calendar", "Settings" };
        int icon[] = {R.drawable.add, R.drawable.add, R.drawable.add};
        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            if (i == 0) {

               item.setTag("Add");
            }
            else if (i == 1) {
                item.setTag("Calendar");
            }
            else {
                item.setTag("Settings");
            }


            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    System.out.println(tag);
                    if (tag == "Add") {
                        final ParseObject newStudent = new ParseObject("Students");
                        newStudent.put("Name", "");
                        newStudent.put("Grade", "");
                        ArrayList<String> sampleList = new ArrayList<>();
                        sampleList.add("No Homework");
                        newStudent.put("Homework", sampleList);
                        newStudent.saveInBackground();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        //builder.setTitle("New Student").setMessage("Time to add a new student");
                        builder.setTitle("New Student");
                        final CharSequence[] array = {"Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12"};






                        final View view;
                        LayoutInflater linf = getLayoutInflater();

                        view = linf.inflate(R.layout.alert, null);
                        builder.setView(view);

                        builder.setSingleChoiceItems(array, 2, new DialogInterface.OnClickListener() {
                            String choice;

                            @Override
                            public void onClick(DialogInterface dialog, final int which) {

                                EditText text = (EditText) view.findViewById(R.id.editText);

                                final String nameText = text.getText().toString();
                                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Students");
                                query.getInBackground(newStudent.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        parseObject.put("Grade", array[which]);
                                        parseObject.put("Name", nameText);
                                        parseObject.saveInBackground();
                                    }
                                });
                            }

                        });

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                               EditText text = (EditText) view.findViewById(R.id.editText);

                               final String nameText = text.getText().toString();
                               ParseQuery q = ParseQuery.getQuery("Students");
                               q.getInBackground(newStudent.getObjectId(), new GetCallback<ParseObject>() {

                                   @Override
                                   public void done(ParseObject parseObject, ParseException e) {

                                       parseObject.put("Name", nameText);
                                       parseObject.saveInBackground();
                                   }


                               });
                               dialog.dismiss();
                               loadStudents();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newStudent.deleteInBackground();
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();

                        dialog.show();
                    }
                    else if (tag == "Calendar") {

                    }
                    else {

                    }
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT

        }

        loadStudents();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_reload) {
            loadStudents();
        }
        if (id == R.id.nice_view) {
            Intent i = new Intent(this, NiceViewActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }



    private class SchoolAdapter extends BaseAdapter {

        private ArrayList<Student> s;
        
        public SchoolAdapter(ArrayList<Student> s) {
            this.s = s;
        }

        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return s.size();
        }

        @Override
        public Object getItem(int position) {
            return s.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.element, parent, false);
            }

            String name = s.get(position).getName();
            String grade = s.get(position).getGrade();
            List<String> homework = s.get(position).getHomework();
            System.out.println("Homework");

            TextView nameText = (TextView) convertView.findViewById(R.id.textView);
            TextView gradeText = (TextView) convertView.findViewById(R.id.textView2);
            TextView homeworkText = (TextView) convertView.findViewById(R.id.textView4);





            String homeworkTextString = "Current Homework: " + homework.get(0);

            nameText.setText(name);
            gradeText.setText(grade);
            homeworkText.setText(homeworkTextString);

            return convertView;
        }
    }

    public void clickSetUp(final ArrayList<Student> s, final ArrayList<ParseObject> objects) {
        final StaggeredGridView listView = (StaggeredGridView) findViewById(R.id.grid_view);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println(s.get(position).getName());
                    AlertDialog presentDialog = studentOptions(s.get(position).getName(), s.get(position).getGrade(), s.get(position).getHomework(), objects.get(position));
                    presentDialog.show();
                }
            });



    }


}
