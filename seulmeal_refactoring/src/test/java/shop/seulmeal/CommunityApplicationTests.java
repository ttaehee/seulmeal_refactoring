package shop.seulmeal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import shop.seulmeal.common.Search;
import shop.seulmeal.service.community.CommunityService;
import shop.seulmeal.service.domain.Comment;
import shop.seulmeal.service.domain.Like;
import shop.seulmeal.service.domain.Post;
import shop.seulmeal.service.domain.Relation;
import shop.seulmeal.service.domain.Report;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.mapper.CommunityMapper;

@SpringBootTest
@Transactional
class CommunityApplicationTests {

	@Autowired
	private CommunityMapper communityMapper;
	
	@Autowired 
	private CommunityService communityService;

	//@Test : o
	void insertPost() {
		User user = new User();
		user.setUserId("ghm8614"); 

		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		//post.setPostStatus("0");

		assertThat(communityMapper.insertPost(post)).isEqualTo(1);
	}
	
	
	//@Test : o
	void getPost() {
		
		// ==== 준비 ====
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 

		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		//post.setPostStatus("0");
		communityMapper.insertPost(post);
		
		// ==== 검증 ====
		assertThat(communityMapper.getPost(post.getPostNo()).getUser().getUserId()).isEqualTo("ghm8614");
	}
	
	//@Test : o
	void getPost_S() {
		// ==== 준비 ====
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 

		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		//post.setPostStatus("0");
		communityService.insertPost(post);
		
		// ==== 검증 ====
		assertThat(communityService.getPost(post.getPostNo()).getViews()).isEqualTo(1);
		}
	
	//@Test:o
	void getListPost_getPostTotalCount() {
		
		communityMapper.deletePostAll();

		// post1
		User user01 = new User();
		user01.setUserId("ghm8614"); 

		Post post01 = new Post();
		post01.setUser(user01);
		post01.setTitle("제목1");
		post01.setContent("내용1");
		post01.setPostStatus("0");
		
		communityMapper.insertPost(post01);
		
		// post2
		User user02 = new User();
		user02.setUserId("ghm8614"); 

		Post post02 = new Post();
		post02.setUser(user02);
		post02.setTitle("제목2");
		post02.setContent("내용2");
		post02.setPostStatus("0");
		
		communityMapper.insertPost(post02);
		communityMapper.postLikeCountUp(post02.getPostNo());	// 좋아요 +1
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchKeyword("목");
		search.setSearchCondition("2");	// 좋아요순 정렬
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("search",search);
		//map.put("userId","ghm8614");
		
		assertThat(communityMapper.getPostTotalCount(map)).isEqualTo(2);
		assertThat(communityMapper.getListPost(map).size()).isEqualTo(2);
	}
	
	@Test
	void getListPost_S() {
		
		communityMapper.deletePostAll();

		// post1
		User user01 = new User();
		user01.setUserId("ghm8614"); 

		Post post01 = new Post();
		post01.setUser(user01);
		post01.setTitle("제목1");
		post01.setContent("내용1");
		
		communityService.insertPost(post01);
		
		// post2
		User user02 = new User();
		user02.setUserId("ghm8614"); 

		Post post02 = new Post();
		post02.setUser(user02);
		post02.setTitle("제목2");
		post02.setContent("내용2");
		
		communityService.insertPost(post02);
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchKeyword("목");
		search.setSearchCondition("2");	// 좋아요순 정렬
		
		Map<String,Object> map = communityService.getListPost(search, "ghm8614");
		
		assertThat(((List<Post>)map.get("postList")).size()).isEqualTo(2);
		assertThat(map.get("postTotalCount")).isEqualTo(2);
	}
	

	//@Test : o
	void updateDeletePost() {
		
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 

		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		//post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		post.setTitle("제목 수정");
		post.setContent("내용 수정");
		
		assertThat(communityMapper.updatePost(post)).isEqualTo(1);
		assertThat(communityMapper.deletePost(post.getPostNo())).isEqualTo(1);
	}

	//@Test:o
	void postViewsUp() {
		
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 

		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		
		communityMapper.insertPost(post);
		
		assertThat(communityMapper.postViewsUp(post.getPostNo())).isEqualTo(1);
		assertThat(communityMapper.getPost(post.getPostNo()));
		
	}
	
	
	// CommentTest
	//@Test : o
	void insertComment() {
		
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		user.setNickName("ghm");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setPostNo(post.getPostNo());
		comment.setContent("댓글내용 test");
		comment.setLayer("0");
		//comment.setParentCommentNo();
		
		// 
		assertThat(communityMapper.insertComment(comment)).isEqualTo(1);
		
		
	}
	//@Test : o
	void getListTotalCount_getListComment() {
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		user.setNickName("ghm");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		//
		Comment comment01 = new Comment();
		comment01.setUser(user);
		comment01.setPostNo(post.getPostNo());
		comment01.setContent("댓글내용 test01");
		comment01.setLayer("0");
		communityMapper.insertComment(comment01);
		
		Comment comment02 = new Comment();
		comment02.setUser(user);
		comment02.setPostNo(post.getPostNo());
		comment02.setContent("댓글내용 test02");
		comment02.setLayer("0");
		communityMapper.insertComment(comment02);

		Search search = new Search();
		search.setCurrentPage(2);	// 현재 페이지 2
		search.setPageSize(3);	// 한 페이지당 3개 댓글 display
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("search", search);
		map.put("postNo", post.getPostNo());
		
		// 총 댓글 2개
		assertThat(communityMapper.getCommentTotalCount(post.getPostNo())).isEqualTo(2);
		// 2 페이지에서 보일 댓글 수는 0 
		assertThat(communityMapper.getListComment(map).size()).isEqualTo(0); 
	}
	
	//@Test : o
	void getListComment_S() {
		
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		user.setNickName("ghm");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityService.insertPost(post);
		
		//
		Comment comment01 = new Comment();
		comment01.setUser(user);
		comment01.setPostNo(post.getPostNo());
		comment01.setContent("댓글내용 test01");
		comment01.setLayer("0");
		communityService.insertComment(comment01);
		
		Comment comment02 = new Comment();
		comment02.setUser(user);
		comment02.setPostNo(post.getPostNo());
		comment02.setContent("댓글내용 test02");
		comment02.setLayer("0");
		communityService.insertComment(comment02);

		Search search = new Search();
		search.setCurrentPage(1);	// 현재 페이지 1
		search.setPageSize(2);	// 한 페이지당 2개 댓글 display
		
		Map<String,Object> map = communityService.getListcomment(search, post.getPostNo());
		
		assertThat(((List<Comment>)map.get("commentList")).size()).isEqualTo(2);
		assertThat(map.get("commentTotalCount")).isEqualTo(2);
		 
	}
	
	
	
	//@Test : o
	void updateComment() {
		communityMapper.deletePostAll();

		//
		User user = new User();
		user.setUserId("ghm8614"); 
		user.setNickName("ghm");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		//
		Post resultPost = communityMapper.getPost(post.getPostNo());
		
		Comment comment01 = new Comment();
		comment01.setUser(user);
		comment01.setPostNo(resultPost.getPostNo());
		comment01.setContent("댓글내용 test01");
		comment01.setLayer("0");
		comment01.setStatus("0");
	
		communityMapper.insertComment(comment01);
		
		comment01.setContent("댓글 내용 업데이트");
		assertThat(communityMapper.updateComment(comment01)).isEqualTo(1);
	}
	
	//@Test : o
	void deleteComment() {
		communityMapper.deletePostAll();

		//
		User user = new User();
		user.setUserId("ghm8614"); 
		user.setNickName("ghm");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		//
		Comment comment01 = new Comment();
		comment01.setUser(user);
		comment01.setPostNo(post.getPostNo());
		comment01.setContent("댓글내용 test01");
		comment01.setLayer("0");
	
		communityMapper.insertComment(comment01);
		
		//
		assertThat(communityMapper.deleteComment(comment01.getCommentNo())).isEqualTo(1);
//		assertThat(communityMapper.getListComment(post.getPostNo()).size()).isEqualTo(0);
	}
	
	//@Test : o
	void report() {
		
		// ==== 준비 ====
		communityMapper.deleteReportPostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		// ==== 준비 ====
		Report report = new Report();
		report.setReporterId("jeong");
		report.setPostNo(post.getPostNo());
		report.setReason("거짓정보");

		
		Search search = new Search();
		search.setCurrentPage(2);
		search.setPageSize(3);
		
		// ==== 검증 ====
		assertThat(communityMapper.insertReportPost(report)).isEqualTo(1);
		assertThat(communityMapper.getListReportPost(search).size()).isEqualTo(0);
		assertThat(communityMapper.deleteReportPost(post.getPostNo())).isEqualTo(1);
	}
	
	//@Test:o
	void report_S() {
		// ==== 준비 ====
		communityMapper.deleteReportPostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityService.insertPost(post);
		
		// ==== 준비 ====
		Report report = new Report();
		report.setReporterId("jeong");
		report.setPostNo(post.getPostNo());
		report.setReason("거짓정보");
		
		assertThat(communityService.insertReportPost(report)).isEqualTo(1);

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		
		Map<String, Object> map = communityService.getListReportPost(search);
		
		assertThat(map.get("reportTotalCount")).isEqualTo(1);
		assertThat(((List<Report>)map.get("reportList")).size()).isEqualTo(1);
		
	}
	
	
	//@Test : ^
	void like() {
		
		// ==== 준비 ====
		communityMapper.deletePostAll();

		User user = new User();
		user.setUserId("ghm8614"); 
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("제목1");
		post.setContent("내용1");
		post.setPostStatus("0");
		
		communityMapper.insertPost(post);
		
		// ==== 준비 ====
		Like like = new Like();
		like.setUserId("jeong");
		like.setPostNo(post.getPostNo());
		
		// ==== 검증 ====
		// 좋아요
		assertThat(communityMapper.insertLike(like)).isEqualTo(1);
		communityMapper.postLikeCountUp(like.getPostNo());
		//assertThat(communityMapper.getPostLikeCount(post.getPostNo())).isEqualTo(1);
		
		// 좋아요 취소
		assertThat(communityMapper.deleteLike(like)).isEqualTo(1);
		communityMapper.postLikeCountDown(like.getPostNo());
		//assertThat(communityMapper.getPostLikeCount(post.getPostNo())).isEqualTo(0);
	}

	//@Test : o
	void getListFollower_getFollowerTotalCount() {
		
		// ==== 준비 ====
		communityMapper.deleteRelationAll();

		Relation relation01 = new Relation();
		relation01.setRelationStatus("0");
		relation01.setUserId("ghm8614");
		
		User user = new User();
		user.setUserId("minhye");
		
		relation01.setRelationUser(user);
		communityMapper.insertRelation(relation01);
		
		// 
		Relation relation02 = new Relation();
		relation02.setRelationStatus("0");
		relation02.setUserId("jeong");
		relation02.setRelationUser(user);
		communityMapper.insertRelation(relation02);
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchKeyword("8614");
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("relationUserId", "minhye");
		map.put("search", search);
		
		assertThat(communityMapper.getFollowerTotalCount(map)).isEqualTo(1);
		assertThat(communityMapper.getListFollower(map).size()).isEqualTo(1);
	}	
	
	//@Test : o
	void getListFollower_S() {
		
		// ==== 준비 ====
		communityMapper.deleteRelationAll();

		Relation relation01 = new Relation();
		relation01.setRelationStatus("0");
		relation01.setUserId("ghm8614");
		
		User user = new User();
		user.setUserId("minhye");
		
		relation01.setRelationUser(user);
		communityService.insertFollow(relation01);
		
		// 
		Relation relation02 = new Relation();
		relation02.setRelationStatus("0");
		relation02.setUserId("jeong");
		relation02.setRelationUser(user);
		communityService.insertFollow(relation02);
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
//		search.setSearchKeyword("8614");
		
		Map<String,Object> map = communityService.getListFollower(search, "minhye");
		
		assertThat(((List<Relation>)map.get("followerList")).size()).isEqualTo(2);
		assertThat(map.get("followerTotalCount")).isEqualTo(2);
	}	


	//@Test : o
	void getListRelation_getRelationTotalList() {
		
		// ==== 준비 ====
		communityMapper.deleteRelationAll();

		Relation relation = new Relation();
		relation.setUserId("ghm8614");
		relation.setRelationStatus("1");
		
		User user = new User();
		user.setUserId("jeong");
		relation.setRelationUser(user);
		assertThat(communityMapper.insertRelation(relation)).isEqualTo(1);
		

		user.setUserId("minhye");
		relation.setRelationUser(user);
		assertThat(communityMapper.insertRelation(relation)).isEqualTo(1);
	
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		
		search.setSearchKeyword("j");
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", "ghm8614");
		map.put("relationStatus", relation.getRelationStatus());
		map.put("search", search);
		
		assertThat(communityMapper.getListRelation(map).size()).isEqualTo(1);
		assertThat(communityMapper.getRelationTotalCount(map)).isEqualTo(1);	
	}
	
	//@Test : o
	void deleteRelation() {
		
		communityMapper.deleteRelationAll();
		
		Relation relation = new Relation();
		relation.setRelationStatus("1");
		relation.setUserId("ghm8614");
		
		User user = new User();
		user.setUserId("jeong");
		relation.setRelationUser(user);
		
		communityMapper.insertRelation(relation);
		assertThat(communityMapper.deleteRelation(relation)).isEqualTo(1);
	}
	
	void contextLoads() {
	}

}
