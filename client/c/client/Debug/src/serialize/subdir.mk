################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../src/serialize/byteHelper.c \
../src/serialize/derializer.c \
../src/serialize/serializer.c \
../src/serialize/strHelper.c \
../src/serialize/structHelper.c 

OBJS += \
./src/serialize/byteHelper.o \
./src/serialize/derializer.o \
./src/serialize/serializer.o \
./src/serialize/strHelper.o \
./src/serialize/structHelper.o 

C_DEPS += \
./src/serialize/byteHelper.d \
./src/serialize/derializer.d \
./src/serialize/serializer.d \
./src/serialize/strHelper.d \
./src/serialize/structHelper.d 


# Each subdirectory must supply rules for building sources it contributes
src/serialize/%.o: ../src/serialize/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


