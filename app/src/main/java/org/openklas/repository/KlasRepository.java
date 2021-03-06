package org.openklas.repository;

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

import androidx.annotation.NonNull;

import org.openklas.klas.model.Board;
import org.openklas.klas.model.Home;
import org.openklas.klas.model.OnlineContentEntry;
import org.openklas.klas.model.Semester;
import org.openklas.klas.model.SyllabusSummary;

import java.util.List;

import io.reactivex.Single;

public interface KlasRepository {
	Single<String> performLogin(@NonNull String username, @NonNull String password, boolean rememberMe);
	Single<Home> getHome(@NonNull String semester);
	Single<Semester[]> getSemesters();
	Single<Board> getNotices(String semester, String subjectId, int page);
	Single<Board> getQnas(String semester, String subjectId, int page);
	Single<Board> getLectureMaterials(String semester, String subjectId, int page);
	Single<List<SyllabusSummary>> getSyllabusList(int year, int term, String keyword, String professor);
	Single<OnlineContentEntry[]> getOnlineContentList(String semester, String subjectId);
}
