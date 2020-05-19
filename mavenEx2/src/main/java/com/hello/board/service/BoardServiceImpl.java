package com.hello.board.service;
import com.hello.board.dao.BoardDao;
public class BoardServiceImpl implements BoardService{
	private BoardDao boardDao;
	
	public void setBoardDao(BoardDao boardDao) {
		this.boardDao=boardDao;
	}
	
	@Override
	public boolean createNewBoard () {System.out.println("Call BoardServieImpl.createNewBoard()");
return false;
}
}