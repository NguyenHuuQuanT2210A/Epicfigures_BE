//package com.example.productservice.services;
//
//import com.example.productservice.dto.FeedbackDTO;
//import com.example.productservice.entities.Clinic;
//import com.example.productservice.entities.Feedback;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.User;
//import com.example.productservice.repositories.ClinicRepository;
//import com.example.productservice.repositories.FeedbackRepository;
//import com.example.productservice.repositories.ProductRepository;
//import com.example.productservice.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class FeedbackServiceImpl implements FeedbackService {
//
//    private final FeedbackRepository feedbackRepository;
//    private final FeedbackMapper feedbackMapper;
//    private final ClinicRepository clinicRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//
//
//    @Override
//    public List<FeedbackDTO> getAllFeedbackByProductId(long productId) {
//            Optional<Product> product = productRepository.findById(productId);
//            if(product.isEmpty()){
//                throw new RuntimeException("Không tìm thấy product");
//            }
//            List<Feedback> feedbackList = feedbackRepository.findByProduct(product.get());
//            return feedbackList.stream()
//                    .map(feedbackMapper::toDTO)
//                    .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<FeedbackDTO> getAllFeedbackByClinicId(long clinicId) {
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        if(clinic.isEmpty()){
//            throw new RuntimeException("Không tìm thấy clinic");
//        }
//        List<Feedback> feedbackList = feedbackRepository.findByClinic(clinic.get());
//        return feedbackList.stream()
//                .map(feedbackMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<FeedbackDTO> getAllFeedbackByUserId(long userId) {
//        Optional<User> user = userRepository.findById(userId);
//        if(user.isEmpty()){
//            throw new RuntimeException("Không tìm thấy clinic");
//        }
//        List<Feedback> feedbackList = feedbackRepository.findByUser(user.get());
//        return feedbackList.stream()
//                .map(feedbackMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void addFeedback(FeedbackDTO feedbackDTO) {
//        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
//        feedback.setCreateAt(LocalDateTime.now());
//        feedbackRepository.save(feedback);
//    }
//
//    @Override
//    public void updateFeedback(long id, FeedbackDTO feedbackDTO) {
//        Feedback existingFeedback = feedbackRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Feedback not found"));
//
//        if (feedbackDTO.getComment() != null) {
//            existingFeedback.setComment(feedbackDTO.getComment());
//        }
//        if (feedbackDTO.getRating() > 0) {
//            existingFeedback.setRating(feedbackDTO.getRating());
//        }
//        feedbackDTO.setCreateAt(LocalDateTime.now());
//
//        existingFeedback.setProduct(existingFeedback.getProduct());
//        existingFeedback.setClinic(existingFeedback.getClinic());
//        existingFeedback.setUser(existingFeedback.getUser());
//
//        feedbackRepository.save(existingFeedback);
//    }
//
//    @Override
//    public void deleteFeedback(long id) {
//        feedbackRepository.deleteById(id);
//    }
//
//    @Override
//    public List<FeedbackDTO> getFeedbackByRating(int rating) {
//        List<Feedback> feedbackList = feedbackRepository.findByRating(rating);
//        return feedbackList.stream()
//                .map(feedbackMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//
//}
