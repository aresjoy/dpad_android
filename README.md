# dpad_android


##  최신 버전 
  * v2.0.0(2022.07.08)
      * UI를 기존 단순 리스트에서 탭으로 나눔 
  
##  적용 방법 

아래코드를 app 모듈의 gradle에 추가합니다.

```
repositories {
    flatDir {
        dirs 'libs'
    }
}
dependencies { 
    //--------------추가 해야될 library
    implementation(name: 'dpad_aos_sdk_2.0.0-release', ext: 'aar')
    implementation(name: 'okgo-release', ext: 'aar')
    implementation(name: 'okgo_extends-release', ext: 'aar')
    implementation 'com.github.hackware1993:MagicIndicator:1.7.0'
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
    implementation "com.squareup.okhttp3:okhttp:4.2.2"
    implementation "com.google.code.gson:gson:2.8.6"
    implementation 'androidx.appcompat:appcompat:1.4.2'
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
   //----------------
}
```

모듈 디렉토리 app/libs 폴더에 
*dpad_aos_sdk_2.0.0-release.aar*
*okgo-release.aar*
*okgo_extends-release.aar*
파일을 추가합니다.


##  사용방법 

```
    //초기화 
    DPAD.init(this, "-", "-", System.currentTimeMillis().toString());
```
```
    // 적립화면 열기 
    DPAD.showOfferwall(this@MainActivity)
```
