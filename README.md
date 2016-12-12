第08回大阪Jenkins勉強会「Jenkins & Docker」デプロイ用サンプルアプリ
==============================================

## What's this ?


「[Jenkins勉強会大阪第08回](https://connpass.com/event/44408/)」の「[「JenkinsとDockerって何が良いの？ 〜言うてる俺もわからんわ〜]()」のデモで使用したサンプルアプリケーションです。


---

元の名前は `"OpenDocument Spreadsheet" edit sample by Java`。

OpenDocument Spreadsheetを使った「帳票テンプレート」サンプルアプリをFork/改造したもの。

Fork元のリポジトリは https://github.com/kazuhito-m/java-odf-edit-sample

以下のファクタを利用している。

- JOpenDocument(Javaのライブラリ)
- Spring boot
- Doma2
- H2Databaseを使ったDBテスト(本番はMySQL想定)
- Flyway(DBマイグレーションツール)
- Lombok
- Twitter Bootstrap
- Selenid
- Jenkinsfile

## Usage

### Requirement

以下を前提とします。

- JDK8インストール

### Build and Run

- MySQLのDBを用意
    + [DBの用意手順(with Docker)](./INITIAL_DATABASE.md)
- SpringBootのアプリ起動コマンドを叩く
```
./gradlew clean bootRun
```
- ブラウザからURLを指定
    + [http://localhost:8080](http://localhost:8080)

### Unit Test

```
./gradlew clean test
```

### UI Test(Integration Test)

```
./gradlew clean integrationTest
```


## Author

Kazuhito Miura ( [@kazuhito_m](https://twitter.com/kazuhito_m) on Twitter )
