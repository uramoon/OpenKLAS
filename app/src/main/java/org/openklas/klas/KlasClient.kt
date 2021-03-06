package org.openklas.klas

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.util.Base64
import com.google.gson.Gson
import io.reactivex.Single
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.klas.model.Board
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Semester
import org.openklas.klas.model.SyllabusSummary
import org.openklas.klas.request.RequestHome
import org.openklas.klas.request.RequestOnlineContents
import org.openklas.klas.request.RequestPostList
import org.openklas.klas.request.RequestSyllabusSummary
import org.openklas.klas.service.KlasService
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class KlasClient @Inject constructor(
	private val service: KlasService,
	private val gson: Gson
) {
	fun login(username: String, password: String): Single<String> {
		val securityResponse = service.loginSecurity()
		return securityResponse.flatMap { security ->
			val keyFactory = KeyFactory.getInstance("RSA")
			val keySpec = X509EncodedKeySpec(Base64.decode(security.publicKey, Base64.DEFAULT))
			val publicKey = keyFactory.generatePublic(keySpec)

			val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
			cipher.init(Cipher.ENCRYPT_MODE, publicKey)

			val cipherText = cipher.doFinal(gson.toJson(mapOf(
				"loginId" to username,
				"loginPwd" to password,
				"storeIdYn" to "N"
			)).toByteArray())

			val confirmResponse = service.loginConfirm(mapOf(
				"loginToken" to Base64.encodeToString(cipherText, Base64.NO_WRAP),
				"redirectUrl" to "",
				"redirectTabUrl" to ""
			))

			confirmResponse.flatMap { confirm ->
				if(confirm.errorCount > 0) {
					Single.error(KlasSigninFailError("failed login attempt"))
				}else{
					Single.just(confirm.response.userId)
				}
			}
		}
	}

	fun getHome(semester: String): Single<Home> {
		return service.home(RequestHome(semester))
	}

	fun getSemesters(): Single<Array<Semester>> {
		return service.semesters()
	}

	fun getNotices(semester: String, subjectId: String, page: Int): Single<Board> {
		return service.notices(RequestPostList(
			page = page, subject = subjectId, semester = semester
		))
	}

	fun getLectureMaterials(semester: String, subjectId: String, page: Int): Single<Board> {
		return service.materials(RequestPostList(
			page = page, subject = subjectId, semester = semester
		))
	}

	fun getQnas(semester: String, subjectId: String, page: Int): Single<Board> {
		return service.qnas(RequestPostList(
			page = page, subject = subjectId, semester = semester
		))
	}

	fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Single<List<SyllabusSummary>> {
		return service.syllabusList(RequestSyllabusSummary(
			year = year, term = term, keyword = keyword, professor = professor))
	}

	fun getOnlineContentList(semester: String, subjectId: String): Single<Array<OnlineContentEntry>> {
		return service.onlineContentList(RequestOnlineContents(
			semester = semester, subjectId = subjectId
		))
	}
}
