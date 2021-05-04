package com.study.hometrainingkotlin.view.Login

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.study.hometrainingkotlin.BottomNaviView
import com.study.hometrainingkotlin.model.Login_Data
import com.study.hometrainingkotlin.model.externalrepository.utils.LoginInterface
import com.study.hometrainingkotlin.model.externalrepository.utils.RetrofitClient
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.localrepository.room.DataBaseCopy
import com.study.hometrainingkotlin.view.Register.Register
import com.study.hometrainingkotlin.static.LoginLog
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG: String = "hashKey"
    }

    private var BTN_login_Register: Button? = null
    private var BTN_login_Login: Button? = null
    private var TV_login_restricted: TextView? = null
    private var ET_login_Id: EditText? = null
    private var ET_login_Password: EditText? = null
    private lateinit var checkBoxSharedPreference: CheckBox
    private lateinit var idpwSharedPreference: SharedPreferences
    private var editor: Editor? = null
    //private var closelogin: BackPressCloseHandler = BackPressCloseHandler(this)

    //public var userinfo:Bundle?=null
    // public var exercise_settings : Fragment? = Exercise_Settings()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)
        TV_login_restricted = findViewById<View>(R.id.TV_login_restricted) as TextView
        TV_login_restricted!!.setOnClickListener(this)

        BTN_login_Register = findViewById<View>(R.id.BTN_login_Register) as Button
        BTN_login_Register!!.setOnClickListener(this)

        BTN_login_Login = findViewById<View>(R.id.BTN_login_Login) as Button
        BTN_login_Login!!.setOnClickListener(this)

        ET_login_Id = findViewById<View>(R.id.ET_login_Id) as EditText
        ET_login_Password = findViewById<View>(R.id.ET_login_Password) as EditText

        //checkBox
        checkBoxSharedPreference = findViewById<View>(R.id.CB_login_Save) as CheckBox
        checkBoxSharedPreference!!.setOnClickListener(this)

        //preference
        idpwSharedPreference = getSharedPreferences("getidpw", MODE_PRIVATE)
        editor = idpwSharedPreference.edit()


        //preference에 저장한 값을 불러오기 부분
        val id: String = idpwSharedPreference.getString("id", "").toString()
        val pw: String = idpwSharedPreference.getString("pw", "").toString()
        val check: Boolean = idpwSharedPreference.getBoolean("check", false)


        //불러온 값을 각 view에 뿌림
        ET_login_Id!!.setText(id)
        ET_login_Password!!.setText(pw)
        checkBoxSharedPreference.isChecked = check


        //SQLite(assets에있는 db파일)파일 복사 시작 부분
        //쓰레드(코루틴 사용)
        var copyStart: Job = CoroutineScope(Dispatchers.IO).launch {
            DataBaseCopy.copyDatabase(context = applicationContext)
        }
        runBlocking {
            copyStart.join()
        }
    }


    override fun onClick(v: View?) {
        //체크박스 버튼 클릭시
        if (checkBoxSharedPreference.isChecked() == true) {
            val gid: String
            val gpw: String
            val gcheck: Boolean
            gid = ET_login_Id!!.text.toString()
            gpw = ET_login_Password!!.text.toString()
            gcheck = checkBoxSharedPreference.isChecked
            //editor에 상태 값 넣기
            editor!!.putString("id", gid)
            editor!!.putString("pw", gpw)
            editor!!.putBoolean("check", gcheck)
            //적용
            editor!!.commit()
        } else {
            editor!!.clear()
            editor!!.commit()
        }

        //버튼 클릭시
        when (v!!.id) {
            R.id.TV_login_restricted -> {
                var restricted_login = Intent(this, BottomNaviView::class.java)
                Toast.makeText(this, R.string.restricted_login, Toast.LENGTH_LONG).show()
                restricted_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                restricted_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(restricted_login)
            }
            R.id.BTN_login_Register -> {
                var register = Intent(this, Register::class.java)
                startActivity(register)
            }
            R.id.BTN_login_Login -> {
                var loginHashMapData: HashMap<String, String> = HashMap<String, String>()
                loginHashMapData.put("id", ET_login_Id?.text.toString())
                loginHashMapData.put("nickname", ET_login_Password?.text.toString())

                var loginInterface: LoginInterface = RetrofitClient.RetrofitClient.getInstance().create(LoginInterface::class.java)
                var callLogin: Call<Login_Data>? = loginInterface.post_Login(loginHashMapData)
                if (callLogin != null) {
                    callLogin.enqueue(object : Callback<Login_Data> {
                        override fun onResponse(call: Call<Login_Data>, response: Response<Login_Data>) {
                            Log.d(javaClass.simpleName, "상태코드" + response.code())
                            if (response.isSuccessful) {
                                //서버에서 조회시 아무것도 조회를 하지못하면 null을 반환하기에 만들었음
                                if (response.body()!=null) {
                                    //콜백
                                    var loginResponse: Login_Data = response.body()!!

                                    var idLog: String = loginResponse.id
                                    //pw로 바꿔야함
                                    var pwLog: String = loginResponse.nickname

                                    LoginLog.getInstance()!!.setloginIdLog(idLog)
                                    LoginLog.getInstance()!!.setloginPwLog(pwLog)
                                    val mainActivity: Intent = Intent(this@Login, BottomNaviView::class.java)
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(mainActivity)
                                }else if(response.body()==null){
                                    Toast.makeText(this@Login,"비밀번호 또는 id를 확인하세요",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<Login_Data>, t: Throwable) {
                            Log.d(javaClass.simpleName, "상태코드" + t.message)
                        }

                    }
                    )
                }

            }
        }
    }
}