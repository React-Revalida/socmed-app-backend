package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
}
