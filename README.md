# 講座取得アプリ

# 仕様
* アプリ起動時にローカルのデータベースから取得済みの講座一覧を取得します。
* アプリ起動時に講座一覧取得API一覧を叩きます。
  * API取得成功時に講座の進捗APIを叩きます。
  * API取得失敗時にエラーダイアログを表示し、ローカルのデータベースから取得した講座一覧を表示します。
* 講座の進捗API
  * 講座一覧APIで取得した講座一覧のidを元にAPIを叩きます。
  * API取得成功時に講座の進捗を更新します。
  * API取得失敗時に、該当講座の進捗が最新でない旨の表示をします。
* Pull-to-refreshにより再度API取得を試みます。
* ブックマーク
  * 画面上でチェックボックスをタップすることにより、講座をブックマークに追加できます。
  * アプリ起動時に、ブックマーク状態を含むすべての講座が復元されます。
  * API取得成功時には、保存されたブックマーク状態が反映されます。
* UI
  * 講座の進捗は、`LinearProgressIndicator`により表示されます。

# Environment

* Android Studio `Android Studio Arctic Fox | 2020.3.1 Patch 2`
* kotlin `1.5.31`
* gradle `7.2`
* minSDK `23`

# 使用技術

* `Hilt`: DI
* `OkHttp`, `Retrofit`: APIリクエスト
* `Room`: DB
* `Picasso`: 画像取得
* `Data Binding`: View操作
* `RxJava`: Asynchronous Task