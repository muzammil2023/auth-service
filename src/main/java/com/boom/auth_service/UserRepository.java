package com.boom.auth_service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	// =========================
	// 🔐 BASIC QUERIES
	// =========================

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	long countByEmail(String email);

	// =========================
	// 🔍 NAME SEARCH
	// =========================

	List<User> findByName(String name);

	List<User> findByNameContaining(String name);

	List<User> findByNameStartingWith(String prefix);

	List<User> findByNameEndingWith(String suffix);

	// =========================
	// 🔀 MULTI CONDITIONS
	// =========================

	Optional<User> findByEmailAndName(String email, String name);

	List<User> findByNameOrEmail(String name, String email);

	// =========================
	// 📊 COMPARISONS (if age exists later)
	// =========================

	List<User> findByIdGreaterThan(Long id);

	List<User> findByIdLessThan(Long id);

	List<User> findByIdBetween(Long start, Long end);

	// =========================
	// 📥 IN QUERY
	// =========================

	List<User> findByEmailIn(List<String> emails);

	// =========================
	// 🧠 CUSTOM JPQL QUERIES
	// =========================

	@Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
	List<User> searchByName(@Param("name") String name);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> getUserByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.email = :email AND u.name = :name")
	Optional<User> findExactUser(@Param("email") String email, @Param("name") String name);

	// =========================
	// ⚡ NATIVE SQL QUERIES
	// =========================

	@Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
	Optional<User> findByEmailNative(@Param("email") String email);

	// name = "%name%"
	@Query(value = "SELECT * FROM users WHERE name LIKE :name", nativeQuery = true)
	List<User> searchByNameNative(@Param("name") String name);

	// =========================
	// 🗑 DELETE OPERATIONS
	// =========================

	void deleteByEmail(String email);

	void deleteByName(String name);
}
