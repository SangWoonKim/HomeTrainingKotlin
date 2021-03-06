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
import com.study.hometrainingkotlin.R
import com.study.hometrainingkotlin.model.externalrepository.vo.Login_Data
import com.study.hometrainingkotlin.model.externalrepository.utils.LoginInterface
import com.study.hometrainingkotlin.model.externalrepository.utils.RetrofitClient
import com.study.hometrainingkotlin.model.localrepository.room.DataBaseCopy
import com.study.hometrainingkotlin.static.LoginLog
import com.study.hometrainingkotlin.view.Register.Register
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

        //getHashKey()
        //preference??? ????????? ?????? ???????????? ??????
        val id: String = idpwSharedPreference.getString("id", "").toString()
        val pw: String = idpwSharedPreference.getString("pw", "").toString()
        val check: Boolean = idpwSharedPreference.getBoolean("check", false)


        //????????? ?????? ??? view??? ??????
        ET_login_Id!!.setText(id)
        ET_login_Password!!.setText(pw)
        checkBoxSharedPreference.isChecked = check


        //SQLite(assets????????? db??????)?????? ?????? ?????? ??????
        //?????????(????????? ??????)
        var copyStart: Job = CoroutineScope(Dispatchers.IO).launch {
            DataBaseCopy.copyDatabase(context = applicationContext)
        }
        runBlocking {
            copyStart.join()
        }

//        getHashKey()
    }


    override fun onClick(v: View?) {
        //???????????? ?????? ?????????
        if (checkBoxSharedPreference.isChecked() == true) {
            val gid: String
            val gpw: String
            val gcheck: Boolean
            gid = ET_login_Id!!.text.toString()
            gpw = ET_login_Password!!.text.toString()
            gcheck = checkBoxSharedPreference.isChecked
            //editor??? ?????? ??? ??????
            editor!!.putString("id", gid)
            editor!!.putString("pw", gpw)
            editor!!.putBoolean("check", gcheck)
            //??????
            editor!!.commit()
        } else {
            editor!!.clear()
            editor!!.commit()
        }

        //?????? ?????????
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

                var loginInterface: LoginInterface =
                    RetrofitClient.RetrofitClient.getInstance().create(
                        LoginInterface::class.java
                    )
                var callLogin: Call<Login_Data>? = loginInterface.post_Login(loginHashMapData)
                if (callLogin != null) {
                    callLogin.enqueue(object : Callback<Login_Data> {
                        override fun onResponse(
                            call: Call<Login_Data>,
                            response: Response<Login_Data>
                        ) {
                            Log.d(javaClass.simpleName, "????????????" + response.code())
                            if (response.isSuccessful) {
                                //???????????? ????????? ???????????? ????????? ??????????????? null??? ??????????????? ????????????
                                if (response.body() != null) {
                                    //??????
                                    var loginResponse: Login_Data = response.body()!!

                                    var idLog: String = loginResponse.id
                                    //pw??? ????????????
                                    var pwLog: String = loginResponse.nickname

                                    LoginLog.getInstance()!!.setloginIdLog(idLog)
                                    LoginLog.getInstance()!!.setloginPwLog(pwLog)
                                    val mainActivity: Intent = Intent(
                                        this@Login,
                                        BottomNaviView::class.java
                                    )
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(mainActivity)
                                } else if (response.body() == null) {
                                    Toast.makeText(
                                        this@Login,
                                        "???????????? ?????? id??? ???????????????",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<Login_Data>, t: Throwable) {
                            Log.d(javaClass.simpleName, "????????????" + t.message)
                        }

                    }
                    )
                }
            }
        }
    }

    //???????????? ????????? ????????? ?????? ??????
//    private fun getHashKey() {
//        var packageInfo: PackageInfo? = null
//        try {
//            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
//        for (signature in packageInfo!!.signatures) {
//            try {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            } catch (e: NoSuchAlgorithmException) {
//                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
//            }
//        }
//    }
}