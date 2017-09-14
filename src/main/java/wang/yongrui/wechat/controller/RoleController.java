/**
 * 
 */
package wang.yongrui.wechat.controller;

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

import wang.yongrui.wechat.entity.web.Role;
import wang.yongrui.wechat.service.RoleService;

/**
 * @author Wang Yongrui
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	@PreAuthorize("hasPermission('Role', 'Create')")
	public ResponseEntity<Role> create(@RequestBody Role role) {
		return new ResponseEntity<>(roleService.create(role), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasPermission('Role', 'Retrieve')")
	public ResponseEntity<Role> retrieve(@PathVariable Long id) {
		return new ResponseEntity<>(roleService.retrieveOneById(id), HttpStatus.OK);
	}

	@PostMapping("/page")
	@PreAuthorize("hasPermission('Role', 'Retrieve')")
	public ResponseEntity<Page<Role>> retrieveAllIntoPage(@RequestBody(required = false) Role preciseRole,
			Pageable pageable) {
		return new ResponseEntity<>(roleService.retrieveAllIntoPage(preciseRole, pageable), HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasPermission('Role', 'Update')")
	public ResponseEntity<Role> put(@RequestBody Role role) {
		return new ResponseEntity<>(roleService.putUpdate(role), HttpStatus.OK);
	}

	@PatchMapping
	@PreAuthorize("hasPermission('Role', 'Update')")
	public ResponseEntity<Role> patch(@RequestBody Role role) {
		return new ResponseEntity<>(roleService.patchUpdate(role), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasPermission('Role', 'Delete')")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		return new ResponseEntity<>(roleService.delete(id), HttpStatus.OK);
	}

}
