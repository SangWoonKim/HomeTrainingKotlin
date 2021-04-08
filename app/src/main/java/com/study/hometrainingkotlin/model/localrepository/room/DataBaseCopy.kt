package com.study.hometrainingkotlin.model.localrepository.room

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


//db복사 파일 앱실행과 동시 실행
object DataBaseCopy {
    private val TAG = DataBaseCopy::class.java.simpleName
    private const val DB_NAME ="HomeTrainingKotlin.db"
    private var INSTANCE: AppDatabase?= null

    //https://developer.android.com/training/data-storage/room/prepopulate?hl=ko#from-file
    fun getAppDataBase(context: Context): AppDatabase?{
        if (INSTANCE == null){
            synchronized(AppDatabase::class){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(AppDatabase.MIGRATION)
                    .build()
            }
        }else{

        }
        return INSTANCE
    }

    //assets폴더의 DB를 기기 내부에 설치되는 앱/databases폴더에 복사
    fun copyDatabase(context: Context){
        Log.d(TAG,"DBcopyStart")
        //db경로 저장
        val dbPath = context.getDatabasePath(DB_NAME)
        Log.d(TAG,"DB경로"+dbPath.toString())

        //db파일 존재시
        if (dbPath.exists()){
            //패키지 정보 반환(패키지 이름,버전정보,어플리케이션 정보가 들어있음)
            val info:PackageInfo = context.packageManager.getPackageInfo(context.packageName,0)

            //버전 정보 추출하여 반환
            val version = PackageInfoCompat.getLongVersionCode(info)
            Log.d(TAG,version.toString())

            //버전 관리 (계속 변경)
            //만약 변경하지 않을 경우 주석처리 요망
            //앱 업데이트시 사용되어야할 부분
            if(version.toString() != "1"){
                Log.d(TAG,"different version")
                copyDB(context,dbPath)
            }

            return
        }
        Log.d(TAG,"db does not Exist!")

        //존재하지 않을 시
        //폴더 생성
        dbPath.parentFile.mkdirs()
        copyDB(context,dbPath)
    }


    //복사 메소드
    private fun copyDB(context: Context, filePath:File){
        try {
            //입력스트림에 assets폴더의 해당 .open("파일명")파일 할당
            val inputStream: InputStream = context.assets.open(DB_NAME)
            //출력스트림 즉 파일을 생성할 경로를 할당
            val output = FileOutputStream(filePath)

            //8바이트 단위로 즉 배열한개당 크기가 8192kb
            //8바이트씩 된다고 듣긴했는데.. 원래는 1바이트씩 해야함 안되면 1024로 바꾸기
            val buffer = ByteArray(1024)
            var length:Int

            while (true){
                //해당 범위 만큼 읽어서 length에 저장
                //더 이상 읽을게 없을 경우 .read()는 -1반환
                //.read() len개의 byte를 읽어서 byte[off]에서 부터 저장
                length = inputStream.read(buffer,0,1024)
                //length의 크기가 0이하일 경우 반복정지
                if (length <= 0) break
                //length의 크기만큼 8바이트씩 쓰기
                output.write(buffer,0,length)
            }
            output.flush()
            output.close()
            inputStream.close()
        }catch (e: IOException){
            Log.d(TAG,"copyDB fail",e)
            e.printStackTrace()
        }
    }
}