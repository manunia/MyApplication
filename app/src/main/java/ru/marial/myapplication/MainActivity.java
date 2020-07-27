package ru.marial.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        
        //создание обозреваемого
        final Observable<String> stringObservable = Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        try {
                            String simb = String.valueOf(editText.getText());
                            List<String> strings = Arrays.asList(simb);
                            for (String s:strings) {
                                emitter.onNext(s);
                            }
                            emitter.onComplete();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }
        );

        //создание обозревателя
        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                textView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(e.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };

        //слушатель изменений в текстовой области
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                //подписка на Observable
                stringObservable.subscribe(observer);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });






    }

    private void initFields() {
        editText = findViewById(R.id.edittext);
        textView = findViewById(R.id.textview);
    }
}