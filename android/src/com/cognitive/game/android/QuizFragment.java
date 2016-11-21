package com.cognitive.game.android;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Jiaxing on 20/11/2016.
 */

public class QuizFragment extends Fragment {


    private int[] numbers;
    private int pos = 0;
    private Integer correct = 0;
    private int nBack;
    private int hour_left = QuizActivity.hour_left;
    //private int hour_left = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.quiz_layout, container, false);

        final TextView nText = (TextView) resultView.findViewById(R.id.N_Back_Number); //animation pop up text view
        final Button nextNumBtn = (Button) resultView.findViewById(R.id.next_btn); //initialize n-back question array
        final ImageView quiz_hint = (ImageView) resultView.findViewById(R.id.quiz_instruction); //image instruction button
        final Button corr_btn = (Button) resultView.findViewById(R.id.Correct);
        final Button wron_btn = (Button) resultView.findViewById(R.id.Wrong);

        nBack = QuizActivity.nBack;
        numbers = new QuestionRandomGenerator().generator(nBack);
        Log.i("===Nums array length: ", numbers.length + "");
        Log.i("===Backs: ", nBack + "");
        //nText.animateText(Integer.toString(numbers[pos]));
        nText.setText(Integer.toString(numbers[pos]));

        //click event for next number button
        nextNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                nText.setText("");
                nText.setText(Integer.toString(numbers[pos]));
                if(pos == nBack) {
                    nextNumBtn.setVisibility(View.GONE);
                    corr_btn.setVisibility(View.VISIBLE);
                    wron_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        //click event for correct and wrong button
        corr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(pos < numbers.length) {
                if(numbers[pos] == numbers[pos - nBack]) {
                    correct++;
                    Log.i("===correct", correct+"" + " numbers last: " + numbers[pos - nBack] + " number this: " + numbers[pos] +"");
                }
                Log.i("correct", correct+"" + " numbers last: " + numbers[pos - nBack] + " number this: " + numbers[pos] +"");
                updateTextView(nText);
                //}else
                //FinishQuiz();
            }
        });

        wron_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numbers[pos] != numbers[pos - nBack]) {
                    correct++;
                    Log.i("===correct", correct+"" + " numbers last: " + numbers[pos - nBack] + " number this: " + numbers[pos] +"");
                }
                Log.i("correct", correct+"" + " numbers last: " + numbers[pos - nBack] + " number this: " + numbers[pos] +"");
                updateTextView(nText);
            }
        });

        //click evnet for image construction
        quiz_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateHint(getActivity());
            }
        });

        onCreateHint(getActivity(), hour_left);


        return resultView;
    }

    private void updateTextView(TextView textView){
        pos++;
        if(pos < numbers.length) {
            textView.setText("");
            textView.setText(Integer.toString(numbers[pos]));
        }
        else
            FinishQuiz();
    }

    private void FinishQuiz(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Summary of this quiz");
        builder.setMessage(correct.toString() + " correct among " + (numbers.length - nBack) + "");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QuizActivity.hour_left--;
                Double accurcy = (double) correct / 10.0 * 100;

                QuizActivity.accuracy_list.add(accurcy.intValue());
                FragmentManager fragmentManager = getFragmentManager();

                if(QuizActivity.hour_left != 0){ //if still has hours, back to main story
                    QuizFragment si = new QuizFragment();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main_interface, si);
                    ft.commit();
                }
                else{ //to ending if game is over
                /*    QuizFragment ef = new QuizFragment();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main_interface, ef);
                    ft.commit();
                 */
                    Intent intent = new Intent (getActivity(), AndroidLauncher.class);
                    startActivity(intent);

                }
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void generateHint(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Question Instruction");
        builder.setMessage("This question requires you to finish " + nBack + "-back task, in this task," +
                " you should try to indicate that if the current number matches the one from " +
                nBack + " steps before. If it is, press correct button, otherwise press wrong button. " +
                "You should press next number to start. Good luck traveller." );
        builder.setPositiveButton("Got it!", null);
        builder.setCancelable(false);
        builder.create().show();
    }

    private void onCreateHint(Context context, int hour){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        switch (hour){
            case 5:
                generateHint(context);
                break;

            case 4:
                break;

            case 3:
                builder.setTitle("Three Back!");
                builder.setMessage("Things get a little bit harder, enjoy your 3-back challenge.");
                builder.setPositiveButton("Ok", null);
                builder.setCancelable(false);
                builder.create().show();
                break;

            case 2:
                break;

            case 1:
                builder.setTitle("Four Back!");
                builder.setMessage("Oops! Things are getting insane. Monsters are trying to ask you " +
                        "questions that they even do not know the answer.");
                builder.setPositiveButton("Alright...", null);
                builder.setCancelable(false);
                builder.create().show();
                break;

            default:
                break;
        }
    }




}
