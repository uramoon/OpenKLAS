package org.openklas.data

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

import io.reactivex.Single
import org.openklas.klas.model.Board
import org.openklas.klas.model.Home
import org.openklas.klas.model.OnlineContentEntry
import org.openklas.klas.model.Semester
import org.openklas.klas.model.SyllabusSummary

interface KlasDataSource {
	fun performLogin(username: String, password: String): Single<String>
	fun getHome(semester: String): Single<Home>
	fun getSemesters(): Single<Array<Semester>>
	fun getNotices(semester: String, subjectId: String, page: Int): Single<Board>
	fun getQnas(semester: String, subjectId: String, page: Int): Single<Board>
	fun getLectureMaterials(semester: String, subjectId: String, page: Int): Single<Board>
	fun getSyllabusList(year: Int, term: Int, keyword: String, professor: String): Single<List<SyllabusSummary>>
	fun getOnlineContentList(semester: String, subjectId: String): Single<Array<OnlineContentEntry>>
}
