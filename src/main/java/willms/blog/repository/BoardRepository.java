package willms.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import willms.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	
}