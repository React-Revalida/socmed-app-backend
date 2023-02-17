package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
}
