package willms.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import willms.blog.dto.ReplySaveRequestDto;
import willms.blog.model.Board;
import willms.blog.model.User;
import willms.blog.repository.BoardRepository;
import willms.blog.repository.ReplyRepository;
import willms.blog.repository.UserRepository;

@Service //spring이 component scan 통해서 bean에 등록,IOC함
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private ReplyRepository replyRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패");
				});
	}
	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패");
				});
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		//해당 함수 종료시 트랜잭션이 종료되고, 더티체킹 -> 자동 업데이트
	}	
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replyDto) {
//		User user= userRepository.findById(replyDto.getUserId()).orElseThrow(()->{
//			return new IllegalArgumentException("댓글쓰기 실패 USER");
//		});
//		Board board = boardRepository.findById(replyDto.getBoardId()).orElseThrow(()->{
//			return new IllegalArgumentException("댓글쓰기 실패 BOARD");
//		});
//		Reply reply =  Reply.builder()
//			.user(user)
//			.board(board)
//			.content(replyDto.getContent())
//			.build()
//		;
//		replyRepository.save(reply);
		replyRepository.mSave(replyDto.getUserId(),replyDto.getBoardId(),replyDto.getContent());
	}	
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}
}