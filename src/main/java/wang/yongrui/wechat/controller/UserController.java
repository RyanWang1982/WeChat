/**
 * 
 */
package wang.yongrui.wechat.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wang.yongrui.wechat.entity.web.User;
import wang.yongrui.wechat.service.UserService;
import wang.yongrui.wechat.utils.UserUtils;

/**
 * @author Wang Yongrui
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private UserService userService;

	@PostMapping
	@PreAuthorize("hasPermission('User', 'Create')")
	public ResponseEntity<User> create(@RequestBody User user) {
		return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<User> getCurrentUser() {
		return new ResponseEntity<>(userUtils.getCurrentApplicationUser(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasPermission('User', 'Retrieve')")
	public ResponseEntity<User> retrieve(@PathVariable Long id) {
		return new ResponseEntity<>(userService.retrieveOneById(id), HttpStatus.OK);
	}

	@PostMapping("/page")
	@PreAuthorize("hasPermission('User', 'Retrieve')")
	public ResponseEntity<Page<User>> retrieveAllIntoPage(@RequestBody(required = false) User preciseUser,
			Pageable pageable) {
		return new ResponseEntity<>(userService.retrieveAllIntoPage(preciseUser, pageable), HttpStatus.OK);
	}

	@GetMapping("/role/{roleId}")
	@PreAuthorize("hasPermission('User', 'Retrieve') and hasPermission('Role', 'Retrieve')")
	public ResponseEntity<Page<User>> retrieveAllIntoPageByRoleId(@PathVariable Long roleId, Pageable pageable) {
		return new ResponseEntity<>(userService.retrieveAllIntoPageByRoleId(roleId, pageable), HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasPermission('User', 'Update')")
	public ResponseEntity<User> put(@RequestBody User user) {
		return new ResponseEntity<>(userService.putUpdate(user), HttpStatus.OK);
	}

	@PatchMapping
	@PreAuthorize("hasPermission('User', 'Update')")
	public ResponseEntity<User> patch(@RequestBody User user) {
		return new ResponseEntity<>(userService.patchUpdate(user), HttpStatus.OK);
	}

	@PatchMapping("/mass")
	@PreAuthorize("hasPermission('User', 'Update')")
	public ResponseEntity<Boolean> massPatch(@RequestBody Set<User> userSet) {
		return new ResponseEntity<>(userService.massPatchUpdate(userSet), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasPermission('User', 'Delete')")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
	}

}
