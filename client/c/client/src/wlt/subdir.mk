################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/wlt/WLTServiceClient.cpp 

OBJS += \
./src/wlt/WLTServiceClient.o 

C_DEPS += \
./src/wlt/WLTServiceClient.d 


# Each subdirectory must supply rules for building sources it contributes
src/wlt/%.o: ../src/wlt/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	g++ -O0 -g -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


