package com.shijingfeng.base.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Function: Header OkHttp拦截器
 * Date: 2020/1/22 22:11
 * Description:
 * @author ShiJingFeng
 */
class HeaderInterceptor constructor(private val mHeaderMap: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        for ((key, value) in mHeaderMap) {
            builder.header(key, value)
        }

        //FIXME 此处添加Token, Token获取可以从实体类中, Cookie中 或 本地存储中
        builder.header("token", "")
        request = builder.build()

        return chain.proceed(request)
    }
}