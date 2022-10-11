package com.learn.rxandroidtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<User> observable = getObservable();
        Observer<User> observer = getObserver();

        //đăng kí lắng nghe
        observable.subscribeOn(Schedulers.io()) //xác định thread phát ra dữ liệu
                .observeOn(AndroidSchedulers.mainThread()) //xác định thread mà observe sẽ lắng nghe (nhận dữ liệu từ mainThread)
                .subscribe(observer); //kết nối observable và observer => observe sẽ lắng nghe từ observable
    }

    //tao ra Observer de lang nghe Observable phat ra
    private Observer<User> getObserver() {
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) { //khi observer và observable liên kết và kết nối với nhau (Observer đăng kí lắng nghe Observable thì sẽ nhảy vào hàm này đầu tiên)
                Log.e("Dukay", "onSubscribe");
                disposable = d; //sau khi observable và observer kết nối với nhau thì sẽ trả về 1 disposable
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e("Dukay", "onNext: " + user.toString());
//                if (longNumber == 4) {
//                    disposable.dispose();
//                }

//                if (serializable instanceof User[]) {
//                    User[] users = (User[]) serializable;
//                    for (User user : users) {
//                        Log.e("Dukay", "User infor onNext: " + user.toString());
//                    }
//                } else if (serializable instanceof String) {
//                    String myStr = (String) serializable;
//                    Log.e("Dukay", "String onNext: " + myStr);
//                } else if (serializable instanceof User) {
//                    User user = (User) serializable;
//                    Log.e("Dukay", "User infor onNext: " + user);
//                } else if (serializable instanceof List) {
//                    List<User> userList = (List<User>) serializable;
//                    for (User user : userList) {
//                        Log.e("Dukay", "List data onNext: " + user.toString());
//                    }
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Dukay", "onError");

            }

            @Override
            public void onComplete() {
                Log.e("Dukay", "onComplete");

            }
        };
    }

    private Observable<User> getObservable() {
        List<User> listUser = getListUsers(); //cong viec muon thuc hien la get 1 list user
//        User user1 = new User(1, "Dulay1");
//        User user2 = new User(2, "Dulay2");
//        User user4 = new User(4, "Dulay4");
//        String strData = "Dukay 3";
//        User[] userArr = new User[] {user1, user2};
//        return Observable.rangeLong(1, 10).repeat();

//        //tạo ra 1 observable
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> emitter) throws Throwable {
                //truong hop loi-> thong bao cho ng dung
                if (listUser == null || listUser.isEmpty()) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(new Exception());
                    }
                }

                for (User user : listUser) {
                    if (!emitter.isDisposed()) { //check xem có đnag còn kết nối ko
                        emitter.onNext(user);// nếu còn thì phát ra dữ liệu đó
                    }
                }

                //thông báo cho observer là đã xong cv rồi
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<User> getListUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, "Dukay 1"));
        list.add(new User(2, "Dukay 2"));
        list.add(new User(3, "Dukay 3"));
        list.add(new User(4, "Dukay 4"));
        list.add(new User(5, "Dukay 5"));

        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();

        }
    }
}