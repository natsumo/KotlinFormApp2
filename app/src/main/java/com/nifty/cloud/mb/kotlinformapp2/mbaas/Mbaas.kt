package com.nifty.cloud.mb.kotlinformapp2.mbaas

import com.nifty.cloud.mb.core.NCMBException
import com.nifty.cloud.mb.core.NCMBObject
import com.nifty.cloud.mb.core.NCMBQuery

object Mbaas {
    const val EMAIL = "email"
    const val PREFECTURE = "prefecture"

    /***** demo1：保存  */
    fun saveData(name: String, emailAddress: String, age: Int, prefecture: String, title: String, contents: String, callback: (Any?) -> Unit) {
        try {
            // 保存先クラスの作成
            val inquiry = NCMBObject("Inquiry")
            // データの設定と保存
            inquiry.put("name", name)
            inquiry.put("emailAddress", emailAddress)
            inquiry.put("age", age)
            inquiry.put("prefecture", prefecture)
            inquiry.put("title", title)
            inquiry.put("contents", contents)
            inquiry.saveInBackground { e ->
                if (e != null) {
                    //保存失敗
                    callback(e)
                } else {
                    callback(null)
                }
            }
        } catch (e: NCMBException) {
            callback(e)
        }

    }

    /***** demo2：全件検索  */
    fun getAllData(callback: (List<NCMBObject>?, Any?) -> Unit) {
        // インスタンスの生成
        val query = NCMBQuery<NCMBObject>("Inquiry")
        // 保存日時降順
        query.addOrderByDescending("createDate")
        query.findInBackground { results, e ->
            if (e != null) {
                // 検索失敗
                callback(null, e)
            } else {
                // 検索成功
                callback(results, null)
            }
        }

    }

    /***** demo3-1：条件検索  */
    fun getSearchData(searchBy: String, q: String, callback: (List<NCMBObject>?, Any?) -> Unit) {
        // インスタンスの生成
        val query = NCMBQuery<NCMBObject>("Inquiry")
        // 保存日時降順
        query.addOrderByDescending("createDate")
        // データの条件検索取得（完全一致）
        if (EMAIL == searchBy) {
            query.whereEqualTo("emailAddress", q)
        } else {
            query.whereEqualTo("prefecture", q)
        }
        query.findInBackground { results, e ->
            if (e != null) {
                //検索失敗時の処理
                callback(null, e)
            } else {
                //検索成功時の処理
                callback(results, null)
            }
        }
    }

    /***** demo3-2：条件検索（範囲指定）  */
    fun getRangeSearchData(ageGreaterThan: Int?, ageLessThan: Int?, callback: (List<NCMBObject>?, Any?) -> Unit) {
        // インスタンスの生成
        val query = NCMBQuery<NCMBObject>("Inquiry")
        // 保存日時降順
        query.addOrderByDescending("createDate")
        // データのの条件検索取得（範囲指定）
        query.whereGreaterThanOrEqualTo("age", ageGreaterThan)
        query.whereLessThan("age", ageLessThan)
        query.findInBackground { results, e ->
            if (e != null) {
                // 検索失敗
                callback(null, e)
            } else {
                // 検索成功
                callback(results, null)
            }
        }
    }


}
